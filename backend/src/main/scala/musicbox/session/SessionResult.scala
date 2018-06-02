package musicbox.session

sealed trait SessionResult[+T] {
  def toOption: Option[T]
}

object SessionResult {

  trait SessionValue[T] extends SessionResult[T] {
    def session: T
    def toOption: Option[T] = Some(session)
  }

  trait NoSessionValue[T] extends SessionResult[T] {
    def toOption: Option[T] = None
  }

  case class Decoded[T](session: T) extends SessionResult[T] with SessionValue[T]
  case class DecodedLegacy[T](session: T) extends SessionResult[T] with SessionValue[T]
  case class CreatedFromToken[T](session: T) extends SessionResult[T] with SessionValue[T]

  case object NoSession extends SessionResult[Nothing] with NoSessionValue[Nothing]
  case object TokenNotFound extends SessionResult[Nothing] with NoSessionValue[Nothing]
  case object Expired extends SessionResult[Nothing] with NoSessionValue[Nothing]
  case class Corrupt(e: Exception) extends SessionResult[Nothing] with NoSessionValue[Nothing]

}
