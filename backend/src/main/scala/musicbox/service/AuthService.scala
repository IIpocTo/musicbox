package musicbox.service

import musicbox.db.UserDao
import musicbox.models.Models.{LoginRequest, RegisterRequest, User}
import reactivemongo.bson.BSONObjectID

import scala.concurrent.{ExecutionContext, Future}

class AuthService(userDao: UserDao)(implicit executionContext: ExecutionContext) {

  def login(loginData: LoginRequest): Future[Option[String]] = {
    val user = for {
      user <- userDao.findUserByUsernameAndPassword(loginData.username, loginData.password)
    } yield user
    user.map(_.map(_.id.stringify))
  }

  def register(registerData: RegisterRequest): Future[Boolean] = {
    userDao.insertUser(
      User(
        BSONObjectID.generate(),
        registerData.username,
        registerData.password,
        registerData.email,
        registerData.phone
      )
    )

  }

}
