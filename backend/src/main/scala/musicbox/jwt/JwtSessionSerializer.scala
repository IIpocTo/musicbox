package musicbox.jwt

import musicbox.session.SessionSerializer
import org.json4s._

import scala.util.{Failure, Success, Try}

object JwtSessionSerializer {

  private def failIfNoMatch[T](s: JValue)(pf: PartialFunction[JValue, T]): Try[T] = {
    pf.lift(s).fold[Try[T]](Failure(new Exception(s"Cannot deserialize $s")))(Success(_))
  }

  implicit def stringToJValueSessionSerializer: SessionSerializer[String, JValue] =
    new SessionSerializer[String, JValue] {
      override def serialize(t: String): JValue = JString(t)
      override def deserialize(r: JValue): Try[String] = failIfNoMatch(r) { case JString(v) => v }
    }

  implicit def intToJValueSessionSerializer: SessionSerializer[Int, JValue] =
    new SessionSerializer[Int, JValue] {
      override def serialize(t: Int) = JInt(t)
      override def deserialize(s: JValue) = failIfNoMatch(s) { case JInt(v) => v.intValue() }
    }

  implicit def longToJValueSessionSerializer: SessionSerializer[Long, JValue] =
    new SessionSerializer[Long, JValue] {
      override def serialize(t: Long) = JInt(t)
      override def deserialize(s: JValue) = failIfNoMatch(s) { case JInt(v) => v.longValue() }
    }

  implicit def doubleToJValueSessionSerializer: SessionSerializer[Double, JValue] =
    new SessionSerializer[Double, JValue] {
      override def serialize(t: Double) = JDouble(t)
      override def deserialize(s: JValue) = failIfNoMatch(s) { case JDouble(v) => v }
    }

  def caseClass[T <: Product: Manifest](
    implicit formats: DefaultFormats = DefaultFormats
  ): SessionSerializer[T, JValue] =
    new SessionSerializer[T, JValue] {
      override def serialize(t: T): JValue = Extraction.decompose(t)
      override def deserialize(r: JValue): Try[T] = Try { Extraction.extract[T](r) }
    }

}
