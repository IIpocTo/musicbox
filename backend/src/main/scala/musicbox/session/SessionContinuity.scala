package musicbox.session

import musicbox.session.manager.{ClientSessionManager, RefreshTokenManager, SessionManager}

import scala.concurrent.ExecutionContext

sealed trait SessionContinuity[T] {
  def manager: SessionManager[T]
  def clientSessionManager: ClientSessionManager[T] = manager.clientSessionManager
}

private[session] class OneOff[T](implicit val manager: SessionManager[T])
    extends SessionContinuity[T]

private[session] class Refreshable[T](
  implicit
  val manager: SessionManager[T],
  val refreshTokenStorage: RefreshTokenStorage[T],
  val ec: ExecutionContext
) extends SessionContinuity[T] {

  val refreshTokenManager: RefreshTokenManager[T] =
    manager.createRefreshTokenManager(refreshTokenStorage)
}
