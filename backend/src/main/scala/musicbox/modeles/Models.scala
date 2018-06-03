package musicbox.modeles

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

  case class LoginRequest(username: String, password: String)
  case class RegisterRequest(username: String, password: String, email: String, phone: String)

}
