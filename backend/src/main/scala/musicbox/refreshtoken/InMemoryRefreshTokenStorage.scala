package musicbox.refreshtoken

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.duration.Duration

// Only for testing purpose!!!
trait InMemoryRefreshTokenStorage[T] extends RefreshTokenStorage[T] {

  case class Store(session: T, tokenHash: String, expires: Long)

  private val localStore = mutable.Map[String, Store]()
  def store: Map[String, Store] = localStore.toMap

  def log(msg: String): Unit

  override def lookup(selector: String): Future[Option[RefreshTokenLookupResult[T]]] =
    Future.successful {
      val r = localStore
        .get(selector)
        .map(s => RefreshTokenLookupResult[T](s.tokenHash, s.expires, () => s.session))
      log(s"Looking up token for selector: $selector, found: ${r.isDefined}")
      r
    }

  override def store(data: RefreshTokenData[T]): Future[Unit] = {
    log(
      s"Storing token for selector: ${data.selector}, user: ${data.forSession}, " +
      s"expires: ${data.expires}, now: ${System.currentTimeMillis()}"
    )
    Future.successful(
      localStore.put(data.selector, Store(data.forSession, data.tokenHash, data.expires))
    )
  }

  override def remove(selector: String): Future[Unit] = {
    log(s"Removing token for selector: $selector")
    Future.successful(localStore.remove(selector))
  }

  override def schedule[S](after: Duration)(op: => Future[S]): Unit = {
    log("Running scheduled operation immediately")
    op
    Future.successful(Unit)
  }

}
