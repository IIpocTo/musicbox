package musicbox.session

import musicbox.utils.{Crypto, SessionUtil}

import scala.util.Try

case class DecodeResult[T](t: T, expires: Option[Long], signatureMatches: Boolean)

trait SessionEncoder[T] {
  def encode(t: T, millis: Long, config: SessionConfig): String
  def decode(s: String, config: SessionConfig): Try[DecodeResult[T]]
}

object SessionEncoder {
  implicit def basic[T](implicit serializer: SessionSerializer[T, String]): BasicSessionEncoder[T] =
    new BasicSessionEncoder[T]()
}

class BasicSessionEncoder[T](implicit serializer: SessionSerializer[T, String])
    extends SessionEncoder[T] {

  override def encode(t: T, nowMillis: Long, config: SessionConfig): String = {
    val serialized = "x" + serializer.serialize(t)

    val withExpiry = config.sessionMaxAgeSeconds.fold(serialized) { maxAge =>
      val expiry = nowMillis + maxAge * 1000L
      s"$expiry-$serialized"
    }

    val encrypted =
      if (config.sessionEncryptData) Crypto.encryptAES(withExpiry, config.serverSecret)
      else withExpiry

    s"${Crypto.signHmacSHA1hex(withExpiry, config.serverSecret)}-$encrypted"
  }

  override def decode(s: String, config: SessionConfig): Try[DecodeResult[T]] = {

    def extractExpiry(data: String): (Option[Long], String) = {
      val splitted = data.split("-", 2)
      (Some(splitted(0).toLong), splitted(1))
    }

    def verifySignature(tokenSignature: String, expectedValue: String): Boolean = {
      SessionUtil.constantTimeEquals(
        tokenSignature,
        Crypto.signHmacSHA1hex(expectedValue, config.serverSecret)
      )
    }

    Try {
      val splitted = s.split("-", 2)
      val decrypted =
        if (config.sessionEncryptData) Crypto.decrypt_AES(splitted(1), config.serverSecret)
        else splitted(1)
      val (expiry, serialized) = extractExpiry(decrypted)

      val deserializedResult = serializer.deserialize(serialized.substring(1))

      deserializedResult.map { deserialized =>
        val signatureMatches = verifySignature(splitted(0), decrypted)
        DecodeResult(deserialized, expiry, signatureMatches)
      }
    }.flatten

  }
}
