package musicbox.session.manager

import akka.http.scaladsl.model.headers.HttpCookie
import akka.http.scaladsl.server.AuthorizationFailedRejection
import musicbox.session.SessionConfig
import musicbox.utils.SessionUtil

trait CsrfManager[T] {
  def config: SessionConfig

  def tokenInvalidRejection: AuthorizationFailedRejection.type = AuthorizationFailedRejection
  def createToken(): String = SessionUtil.randomString(64)

  def createCookie() =
    HttpCookie(
      name = config.csrfCookieConfig.name,
      value = createToken(),
      expires = None,
      domain = config.csrfCookieConfig.domain,
      path = config.csrfCookieConfig.path,
      secure = config.csrfCookieConfig.secure,
      httpOnly = config.csrfCookieConfig.httpOnly
    )

}
