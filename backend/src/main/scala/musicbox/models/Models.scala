package musicbox.models

import java.time.LocalDate

import musicbox.db.DBRef
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.Macros.Annotations.Key

object Models {

  case class User(
    @Key("_id") id: BSONObjectID,
    username: String,
    password: String,
    email: String,
    phone: String
  )

  case class Artist(
    @Key("_id") id: BSONObjectID,
    name: String,
    genres: Seq[String],
    image: String,
    popularity: Int,
    albums: Seq[DBRef]
  )

  case class Album(
    @Key("_id") id: BSONObjectID,
    artists: Seq[DBRef],
    name: String,
    image: String,
    releaseDate: Double,
    tracks: Seq[DBRef]
  )

  case class Track(
    @Key("_id") id: BSONObjectID,
    name: String,
    duration: Int,
    album: DBRef,
    content: String,
  )

  case class LoginRequest(username: String, password: String)
  case class RegisterRequest(username: String, password: String, email: String, phone: String)

  case class ArtistResponse(
    id: String,
    name: String,
    genres: Seq[String],
    image: String,
    albumsIds: Seq[String]
  )

  case class TrackResponse(
    id: String,
    name: String,
    duration: Int,
    content: String,
  )

  case class AlbumResponse(
    id: String,
    artistsId: Seq[String],
    name: String,
    image: String,
    releaseDate: LocalDate,
    tracks: Seq[TrackResponse]
  )

}
