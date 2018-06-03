package musicbox.session.directives

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, Directive0, Directive1}
import musicbox.session._

trait OneOffSessionDirectives {

  private def readCookie[T](
    sc: SessionContinuity[T]
  ): Directive[Tuple1[Option[(String, SetSessionTransport)]]] =
    optionalCookie(sc.manager.config.sessionCookieConfig.name)
      .map(_.map(cookie => (cookie.value, CookieST: SetSessionTransport)))

  private def readHeader[T](
    sc: SessionContinuity[T]
  ): Directive[Tuple1[Option[(String, SetSessionTransport)]]] =
    optionalHeaderValueByName(sc.manager.config.sessionHeaderConfig.getFromClientHeaderName)
      .map(_.map(h => (h, HeaderST: SetSessionTransport)))

  private def read[T](
    sc: SessionContinuity[T],
    st: GetSessionTransport
  ): Directive[Tuple1[Option[(String, SetSessionTransport)]]] = {
    st match {
      case CookieST         => readCookie(sc)
      case HeaderST         => readHeader(sc)
      case CookieOrHeaderST => readCookie(sc).flatMap(_.fold(readHeader(sc))(v => provide(Some(v))))
    }
  }

  private[session] def setOneOffSession[T](
    sc: SessionContinuity[T],
    st: SetSessionTransport,
    value: T
  ): Directive0 = {
    st match {
      case CookieST => setCookie(sc.clientSessionManager.createCookie(value))
      case HeaderST => respondWithHeader(sc.clientSessionManager.createHeader(value))
    }
  }

  private[session] def setOneOffSessionSameTransport[T](
    sc: SessionContinuity[T],
    st: GetSessionTransport,
    value: T
  ): Directive0 =
    read(sc, st).flatMap {
      case None             => pass
      case Some((_, setSt)) => setOneOffSession(sc, setSt, value)
    }

  private[session] def oneOffSession[T](
    sc: SessionContinuity[T],
    st: GetSessionTransport
  ): Directive1[SessionResult[T]] = {
    read(sc, st).flatMap {
      case None             => provide(SessionResult.NoSession)
      case Some((value, _)) => provide(sc.clientSessionManager.decode(value))
    }
  }

  private[session] def invalidateOneOffSession[T](
    sc: SessionContinuity[T],
    st: GetSessionTransport
  ): Directive0 = {
    readCookie(sc).flatMap {
      case None =>
        readHeader(sc).flatMap {
          case None    => pass
          case Some(_) => respondWithHeader(sc.clientSessionManager.createHeaderWithValue(""))
        }
      case Some(_) =>
        deleteCookie(sc.clientSessionManager.createCookieWithValue("").copy(maxAge = None))
    }
  }

}
