package musicbox.session.manager

import akka.http.scaladsl.model.headers.{HttpCookie, RawHeader}
import akka.http.scaladsl.server.AuthorizationFailedRejection
import musicbox.session.{SessionConfig, SessionEncoder, SessionResult}

trait ClientSessionManager[T] {
  def config: SessionConfig
  def sessionEncoder: SessionEncoder[T]
  def millis: Long

  def encode(data: T): String = sessionEncoder.encode(data, millis, config)

  def decode(data: String): SessionResult[T] = {
    sessionEncoder
      .decode(data, config)
      .map { decodeResult =>
        val expired = config.sessionMaxAgeSeconds.fold(false)(
          _ => millis > decodeResult.expires.getOrElse(Long.MaxValue)
        )
        if (expired) {
          SessionResult.Expired
        } else if (!decodeResult.signatureMatches) {
          SessionResult.Corrupt(new Exception("Corrupt signature"))
        } else {
          SessionResult.Decoded(decodeResult.t)
        }
      }
      .recover { case ex: Exception => SessionResult.Corrupt(ex) }
      .get
  }

  private def createCookieWithValue(value: String) =
    HttpCookie(
      name = config.sessionCookieConfig.name,
      value = value,
      expires = None,
      maxAge = None,
      domain = config.sessionCookieConfig.domain,
      path = config.sessionCookieConfig.path,
      secure = config.sessionCookieConfig.secure,
      httpOnly = config.sessionCookieConfig.httpOnly
    )

  private def createHeaderWithValue(value: String) =
    RawHeader(name = config.sessionHeaderConfig.sendToClientHeaderName, value = value)

  def createCookie(data: T): HttpCookie = createCookieWithValue(encode(data))
  def createHeader(data: T): RawHeader = createHeaderWithValue(encode(data))

  def sessionMissingRejection: AuthorizationFailedRejection.type = AuthorizationFailedRejection
}
