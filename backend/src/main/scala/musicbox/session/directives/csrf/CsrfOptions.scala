package musicbox.session.directives.csrf

import akka.stream.Materializer
import musicbox.session.manager.{CsrfManager, SessionManager}

trait CsrfCheckMode[T] {
  def manager: SessionManager[T]
  def csrfManager: CsrfManager[T] = manager.csrfManager
}

private[session] class CheckHeader[T](implicit val manager: SessionManager[T])
    extends CsrfCheckMode[T]

private[session] class CheckHeaderAndForm[T](
  implicit
  val manager: SessionManager[T],
  val materializer: Materializer
) extends CsrfCheckMode[T]

object CsrfOptions {

  def checkHeader[T](implicit manager: SessionManager[T]): CheckHeader[T] = new CheckHeader[T]()

  def checkHeaderAndForm[T](
    implicit manager: SessionManager[T],
    materializer: Materializer
  ): CheckHeaderAndForm[T] =
    new CheckHeaderAndForm[T]()
}
