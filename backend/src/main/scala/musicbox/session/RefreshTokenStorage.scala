package musicbox.session

import scala.concurrent.Future
import scala.concurrent.duration.Duration

case class RefreshTokenData[T](forSession: T, selector: String, tokenHash: String, expires: Long)
case class RefreshTokenLookupResult[T](tokenHash: String, expires: Long, createSession: () => T)

trait RefreshTokenStorage[T] {
  def lookup(selector: String): Future[Option[RefreshTokenLookupResult[T]]]
  def store(data: RefreshTokenData[T]): Future[Unit]
  def remove(selector: String): Future[Unit]
  def schedule[S](after: Duration)(op: => Future[S]): Unit
}
