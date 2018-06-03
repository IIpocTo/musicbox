package musicbox.session.manager

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.model.headers.{HttpCookie, RawHeader}
import musicbox.refreshtoken.{RefreshTokenData, RefreshTokenStorage}
import musicbox.session.{SessionConfig, SessionResult}
import musicbox.utils.{Crypto, SessionUtil}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.Duration

trait RefreshTokenManager[T] {
  def config: SessionConfig
  def nowMillis: Long
  def storage: RefreshTokenStorage[T]

  def createSelector(): String = SessionUtil.randomString(16)
  def createToken(): String = SessionUtil.randomString(64)

  def encodeSelectorAndToken(selector: String, token: String): String = s"$selector:$token"

  def decodeSelectorAndToken(value: String): Option[(String, String)] = {
    val s = value.split(":", 2)
    if (s.length == 2) Some((s(0), s(1))) else None
  }

  def rotateToken(session: T, existing: Option[String])(
    implicit ec: ExecutionContext
  ): Future[String] = {

    val selector = createSelector()
    val token = createToken()

    val storeFuture: Future[String] = storage
      .store(
        new RefreshTokenData[T](
          forSession = session,
          selector = selector,
          tokenHash = Crypto.hashSHA256(token),
          expires = nowMillis + config.refreshTokenMaxAgeSeconds * 1000L
        )
      )
      .map(_ => encodeSelectorAndToken(selector, token))

    existing.flatMap(decodeSelectorAndToken).foreach {
      case (s, _) =>
        storage.schedule(Duration(config.removeUsedRefreshTokenAfter, TimeUnit.SECONDS)) {
          storage.remove(s)
        }
    }

    storeFuture

  }

  def createCookie(value: String) =
    HttpCookie(
      name = config.refreshTokenCookieConfig.name,
      value = value,
      expires = None,
      maxAge = Some(config.refreshTokenMaxAgeSeconds),
      domain = config.refreshTokenCookieConfig.domain,
      path = config.refreshTokenCookieConfig.path,
      secure = config.refreshTokenCookieConfig.secure,
      httpOnly = config.refreshTokenCookieConfig.httpOnly
    )

  def createHeader(value: String) =
    RawHeader(name = config.refreshTokenHeaderConfig.sendToClientHeaderName, value = value)

  def sessionFromValue(value: String)(implicit ec: ExecutionContext): Future[SessionResult[T]] = {
    decodeSelectorAndToken(value) match {
      case Some((selector, token)) =>
        storage.lookup(selector).flatMap {
          case Some(lookupResult) =>
            if (lookupResult.expires < nowMillis) {
              storage.remove(selector).map(_ => SessionResult.Expired)
            } else if (!SessionUtil
              .constantTimeEquals(Crypto.hashSHA256(token), lookupResult.tokenHash)) {
              storage
                .remove(selector)
                .map(_ => SessionResult.Corrupt(new RuntimeException("Corrupt token hash")))
            } else {
              Future.successful(SessionResult.CreatedFromToken(lookupResult.createSession()))
            }

          case None =>
            Future.successful(SessionResult.TokenNotFound)
        }
      case None =>
        Future.successful(
          SessionResult.Corrupt(new RuntimeException("Cannot decode selector/token"))
        )
    }
  }

  def removeToken(value: String)(implicit ec: ExecutionContext): Future[Unit] = {
    decodeSelectorAndToken(value) match {
      case Some((s, _)) => storage.remove(s)
      case None         => Future.successful(Unit)
    }
  }

}
