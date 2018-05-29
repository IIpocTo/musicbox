package musicbox.modles

object Models {

  case class User(
    id: String,
    username: String,
    password: String,
    email: String,
    phone: String
  )

}
