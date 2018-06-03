package musicbox.session.directives

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Directive1}
import musicbox.session._

trait SessionDirectives extends OneOffSessionDirectives with RefreshableSessionDirectives {

  def setSession[T](sc: SessionContinuity[T], st: SetSessionTransport, v: T): Directive0 = {
    sc match {
      case _: OneOff[T]      => setOneOffSession(sc, st, v)
      case r: Refreshable[T] => setRefreshableSession(r, st, v)
    }
  }

  def session[T](
    sc: SessionContinuity[T],
    st: GetSessionTransport
  ): Directive1[SessionResult[T]] = {
    sc match {
      case _: OneOff[T]      => oneOffSession(sc, st)
      case r: Refreshable[T] => refreshableSession(r, st)
    }
  }

  def invalidateSession[T](sc: SessionContinuity[T], st: GetSessionTransport): Directive0 = {
    sc match {
      case _: OneOff[T] => invalidateOneOffSession(sc, st)
      case r: Refreshable[T] =>
        invalidateOneOffSession(sc, st) & invalidateRefreshableSession(r, st)
    }
  }

  def optionalSession[T](sc: SessionContinuity[T], st: GetSessionTransport): Directive1[Option[T]] =
    session(sc, st).map(_.toOption)

  def requiredSession[T](sc: SessionContinuity[T], st: GetSessionTransport): Directive1[T] =
    optionalSession(sc, st).flatMap {
      case None       => reject(sc.clientSessionManager.sessionMissingRejection)
      case Some(data) => provide(data)
    }

  def touchOptionalSession[T](
    sc: SessionContinuity[T],
    st: GetSessionTransport
  ): Directive1[Option[T]] = {
    optionalSession(sc, st).flatMap { d =>
      d.fold(pass)(s => setOneOffSessionSameTransport(sc, st, s)) & provide(d)
    }
  }

  def touchRequiredSession[T](sc: SessionContinuity[T], st: GetSessionTransport): Directive1[T] = {
    requiredSession(sc, st).flatMap { d =>
      setOneOffSessionSameTransport(sc, st, d) & provide(d)
    }
  }

}
