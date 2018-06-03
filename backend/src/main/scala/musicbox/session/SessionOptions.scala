package musicbox.session

import musicbox.session.manager.SessionManager

import scala.concurrent.ExecutionContext

object SessionOptions {

  def oneOff[T](implicit manager: SessionManager[T]): OneOff[T] = new OneOff[T]()(manager)

  def refreshable[T](
    implicit
    manager: SessionManager[T],
    refreshTokenStorage: RefreshTokenStorage[T],
    ec: ExecutionContext
  ): Refreshable[T] =
    new Refreshable[T]()(manager, refreshTokenStorage, ec)

  def usingCookies: CookieST.type = CookieST
  def usingHeaders: HeaderST.type = HeaderST
  def usingCookiesOrHeaders: CookieOrHeaderST.type = CookieOrHeaderST

}
