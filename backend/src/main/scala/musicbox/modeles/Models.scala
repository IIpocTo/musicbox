package musicbox.modeles

object Models {

  case class User(
    id: String,
    username: String,
    password: String,
    email: String,
    phone: String
  )

  case class LoginRequest(username: String, password: String)

}
