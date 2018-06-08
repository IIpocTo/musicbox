package musicbox.models

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

  case class LoginRequest(username: String, password: String)
  case class RegisterRequest(username: String, password: String, email: String, phone: String)

  case class ArtistResponse(
    id: String,
    name: String,
    genres: Seq[String],
    image: String,
    albumsIds: Seq[String]
  )

}
