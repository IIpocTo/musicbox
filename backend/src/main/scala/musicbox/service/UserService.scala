package musicbox.service

import musicbox.db.UserDao
import musicbox.models.Models.User

import scala.concurrent.{ExecutionContext, Future}

class UserService(userDao: UserDao)(implicit executionContext: ExecutionContext) {

  def getUser(userId: String): Future[Option[User]] = userDao.findById(userId)

}
