package musicbox

import musicbox.session.{SessionSerializer, SingleValueSessionSerializer}

import scala.util.Try

case class MusicboxSession(username: String)

object MusicboxSession {
  implicit def serializer: SessionSerializer[MusicboxSession, String] =
    new SingleValueSessionSerializer(
      _.username,
      (username: String) =>
        Try {
          MusicboxSession(username)
      }
    )
}
