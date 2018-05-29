package musicbox.session

import java.net.{URLDecoder, URLEncoder}

import scala.util.Try

trait SessionSerializer[T, R] {
  def serialize(t: T): R
  def deserialize(r: R): Try[T]
}

class SingleValueSessionSerializer[T, V](toValue: T => V, fromValue: V => Try[T])(
  implicit valueSerializer: SessionSerializer[V, String]
) extends SessionSerializer[T, String] {

  override def serialize(t: T): String = valueSerializer.serialize(toValue(t))
  override def deserialize(r: String): Try[T] = valueSerializer.deserialize(r).flatMap(fromValue)

}

class MultiValueSessionSerializer[T](
  toMap: T => Map[String, String],
  fromMap: Map[String, String] => Try[T]
) extends SessionSerializer[T, String] {

  import SessionSerializer._

  override def serialize(t: T): String =
    toMap(t)
      .map { case (k, v) => urlDecode(k) + "~" + urlEncode(v) }
      .mkString("&")

  override def deserialize(r: String): Try[T] = Try {
    if (r.isEmpty) Map.empty[String, String]
    else {
      r.split("&")
        .map(_.split("~", 2))
        .map(pair => urlDecode(pair(0)) -> urlDecode(pair(1)))
        .toMap
    }
  }.flatMap(fromMap)

}

object SessionSerializer {

  private[session] def urlEncode(s: String): String = URLEncoder.encode(s, "UTF-8")
  private[session] def urlDecode(s: String): String = URLDecoder.decode(s, "UTF-8")

  implicit def stringToStringSerializer: SessionSerializer[String, String] =
    new SessionSerializer[String, String] {
      override def serialize(t: String): String = urlEncode(t)
      override def deserialize(r: String): Try[String] = Try(urlDecode(r))
    }

  implicit def intToStringSerializer: SessionSerializer[Int, String] =
    new SessionSerializer[Int, String] {
      override def serialize(t: Int): String = urlEncode(t.toString)
      override def deserialize(r: String): Try[Int] = Try(urlDecode(r).toInt)
    }

  implicit def longToStringSerializer: SessionSerializer[Long, String] =
    new SessionSerializer[Long, String] {
      override def serialize(t: Long): String = urlEncode(t.toString)
      override def deserialize(r: String): Try[Long] = Try(urlDecode(r).toLong)
    }

  implicit def doubleToStringSerializer: SessionSerializer[Double, String] =
    new SessionSerializer[Double, String] {
      override def serialize(t: Double): String = urlEncode(t.toString)
      override def deserialize(r: String): Try[Double] = Try(urlDecode(r).toDouble)
    }

  implicit def mapToStringSessionSerializer: SessionSerializer[Map[String, String], String] =
    new MultiValueSessionSerializer[Map[String, String]](identity, Try(_))

}
