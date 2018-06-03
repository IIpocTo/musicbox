package musicbox.session.directives

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, Directive0, Directive1}
import musicbox.session._

trait RefreshableSessionDirectives { this: OneOffSessionDirectives =>

  private def readCookie[T](
    sc: SessionContinuity[T]
  ): Directive[Tuple1[Option[(String, SetSessionTransport)]]] =
    optionalCookie(sc.manager.config.refreshTokenCookieConfig.name)
      .map(_.map(c => (c.value, CookieST: SetSessionTransport)))

  private def readHeader[T](
    sc: SessionContinuity[T]
  ): Directive[Tuple1[Option[(String, SetSessionTransport)]]] =
    optionalHeaderValueByName(sc.manager.config.refreshTokenHeaderConfig.getFromClientHeaderName)
      .map(_.map(h => (h, HeaderST: SetSessionTransport)))

  private def read[T](
    sc: SessionContinuity[T],
    st: GetSessionTransport
  ): Directive1[Option[(String, SetSessionTransport)]] =
    st match {
      case CookieST         => readCookie(sc)
      case HeaderST         => readHeader(sc)
      case CookieOrHeaderST => readCookie(sc).flatMap(_.fold(readHeader(sc))(v => provide(Some(v))))
    }

  private def setRefreshToken[T](sc: Refreshable[T], st: SetSessionTransport, v: T): Directive0 = {
    import sc.ec

    read(sc, st).flatMap { existing =>
      val newToken = sc.refreshTokenManager.rotateToken(v, existing.map(_._1))
      st match {
        case CookieST =>
          val createCookie = newToken.map(sc.refreshTokenManager.createCookie)
          onSuccess(createCookie).flatMap(c => setCookie(c))
        case HeaderST =>
          val createHeader = newToken.map(sc.refreshTokenManager.createHeader)
          onSuccess(createHeader).flatMap(c => respondWithHeader(c))
      }
    }
  }

  private[session] def setRefreshableSession[T](
    sc: Refreshable[T],
    st: SetSessionTransport,
    v: T
  ): Directive0 = setOneOffSession(sc, st, v) & setRefreshToken(sc, st, v)

  private[session] def refreshableSession[T](
    sc: Refreshable[T],
    st: GetSessionTransport
  ): Directive1[SessionResult[T]] = {
    import sc.ec

    oneOffSession(sc, st).flatMap {
      case SessionResult.NoSession | SessionResult.Expired =>
        read(sc, st).flatMap {
          case None => provide(SessionResult.NoSession)
          case Some((v, setSt)) =>
            onSuccess(sc.refreshTokenManager.sessionFromValue(v))
              .flatMap {
                case s @ SessionResult.CreatedFromToken(session) =>
                  setRefreshableSession(sc, setSt, session) & provide(s: SessionResult[T])
                case s => provide(s)
              }
        }
      case s => provide(s)
    }
  }

  private[session] def invalidateRefreshableSession[T](
    sc: Refreshable[T],
    st: GetSessionTransport
  ): Directive0 = {
    import sc.ec

    read(sc, st).flatMap {
      case None => pass
      case Some((v, setSt)) =>
        val deleteTokenOnClient = setSt match {
          case CookieST => deleteCookie(sc.refreshTokenManager.createCookie("").copy(maxAge = None))
          case HeaderST => respondWithHeader(sc.refreshTokenManager.createHeader(""))
        }
        deleteTokenOnClient & onSuccess(sc.refreshTokenManager.removeToken(v))
    }
  }

}
