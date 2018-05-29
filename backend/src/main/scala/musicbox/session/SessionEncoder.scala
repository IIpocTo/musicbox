package musicbox.session

import scala.util.Try

case class DecodeResult[T](t: T, expires: Option[Long], signatureMatches: Boolean)

trait SessionEncoder[T] {
  def encode(t: T, millis: Long, config: SessionConfig): String
  def decode(s: String, config: SessionConfig): Try[DecodeResult[T]]
}
