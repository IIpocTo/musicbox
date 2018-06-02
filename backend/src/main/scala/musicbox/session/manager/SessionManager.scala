package musicbox.session.manager

import musicbox.session.{RefreshTokenStorage, SessionConfig, SessionEncoder}

class SessionManager[T](val config: SessionConfig)(implicit sessionEncoder: SessionEncoder[T]) {
  manager =>

  def nowMillis: Long = System.currentTimeMillis()

  val clientSessionManager: ClientSessionManager[T] = new ClientSessionManager[T] {
    override def config: SessionConfig = manager.config
    override def sessionEncoder: SessionEncoder[T] = manager.sessionEncoder
    override def millis: Long = manager.nowMillis
  }

  def createRefreshTokenManager(_storage: RefreshTokenStorage[T]): RefreshTokenManager[T] =
    new RefreshTokenManager[T] {
      override def config: SessionConfig = manager.config
      override def nowMillis: Long = manager.nowMillis
      override def storage: RefreshTokenStorage[T] = _storage
    }

  val csrfManager: CsrfManager[T] = new CsrfManager[T] {
    override def config: SessionConfig = manager.config
  }

}
