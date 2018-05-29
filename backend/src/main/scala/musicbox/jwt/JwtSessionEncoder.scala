package musicbox.jwt

import java.nio.charset.StandardCharsets
import java.util.Base64

import musicbox.session.{DecodeResult, SessionConfig, SessionEncoder, SessionSerializer}
import musicbox.utils.{Crypto, SessionUtil}
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.util.Try

class JwtSessionEncoder[T](
  implicit serializer: SessionSerializer[T, JValue],
  formats: Formats = DefaultFormats
) extends SessionEncoder[T] {

  private def createHeader: JValue = JObject("alg" -> JString("HS256"), "typ" -> JString("JWT"))

  private def createPayload(t: T, millis: Long, config: SessionConfig): JValue = {

    val expires = config.sessionMaxAgeSeconds
      .map(millis / 1000L + _)
      .map("expires" -> JInt(_))
      .toList

    val serialized = serializer.serialize(t)
    val data = if (config.sessionEncryptData) {
      val serializedWrapper = JObject("v" -> serialized)
      JString(Crypto.encryptAES(compact(render(serializedWrapper)), config.serverSecret))
    } else serialized

    JObject(("data" -> data) :: expires)
  }

  private def extractPayload(p: JValue, config: SessionConfig): Try[(T, Option[Long])] = {

    val expires = p \\ "expires" match {
      case JInt(e) => Some(e.longValue() * 1000L)
      case _       => None
    }

    val rawData = p \\ "data"
    val data = if (config.sessionEncryptData) {
      rawData match {
        case JString(s) => parse(Crypto.decrypt_AES(s, config.serverSecret)) \\ "v"
        case _          => rawData
      }
    } else rawData

    val t = serializer.deserialize(data)
    t.map((_, expires))

  }

  private def encode(jv: JValue): String =
    Base64.getUrlEncoder
      .withoutPadding()
      .encodeToString(compact(render(jv)).getBytes(StandardCharsets.UTF_8))

  private def decode(s: String): Try[JValue] = Try {
    parse(new String(Base64.getUrlDecoder.decode(s), StandardCharsets.UTF_8))
  }

  override def encode(t: T, millis: Long, config: SessionConfig): String = {
    val header = encode(createHeader)
    val payload = encode(createPayload(t, millis, config))
    val base = s"$header.$payload"
    val signature = Crypto.signHmacSHA256base64(base, config.serverSecret)
    s"$base.$signature"
  }

  override def decode(s: String, config: SessionConfig): Try[DecodeResult[T]] =
    Try {
      val cleaned = if (s.startsWith("Bearer")) s.substring(7).trim else s
      val List(header, payload, signature) = cleaned.split("\\.").toList
      val decodedValue = decode(payload)

      for {
        jv           <- decodedValue
        (t, expires) <- extractPayload(jv, config)
      } yield {
        val signatureMatches =
          SessionUtil.constantTimeEquals(
            signature,
            Crypto.signHmacSHA256base64(s"$header.$payload", config.serverSecret)
          )
        DecodeResult(t, expires, signatureMatches)
      }

    }.flatten

}
