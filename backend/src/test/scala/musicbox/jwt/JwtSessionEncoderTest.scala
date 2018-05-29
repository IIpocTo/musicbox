package musicbox.jwt

import musicbox.session.{DecodeResult, SessionConfig}
import org.scalatest.TryValues._
import org.scalatest.{FlatSpec, Matchers}

class JwtSessionEncoderTest extends FlatSpec with Matchers {

  val sessionConfig = new SessionConfig(
    "xxxadfadsfdsfadsfasdfdsafdsfasdfsdfasfdsafsadfasdfadfdsfsdfasdfas3r4wf43f34fw4ef4ffefsergf4ef4",
    null,
    null,
    Some(3200L),
    true,
    null,
    "",
    null,
    null,
    100,
    1000
  )

  "JwtSessionEncoder" should "successfully encode and decode" in {
    import musicbox.jwt.JwtSessionSerializer._

    val encoder = new JwtSessionEncoder[String]()
    val en = encoder.encode("xxxx", 1000, sessionConfig)
    val dec = encoder.decode(en, sessionConfig)
    dec.success.value shouldBe DecodeResult("xxxx", Some(3201000), signatureMatches = true)
  }
}
