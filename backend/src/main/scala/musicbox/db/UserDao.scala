package musicbox.db

import musicbox.db.Connection.musicboxDb
import musicbox.modles.Models.User
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocumentHandler, Macros, document}

import scala.concurrent.{ExecutionContext, Future}

case class UserDao()(implicit ec: ExecutionContext) {

  private def userCollection: Future[BSONCollection] = musicboxDb.map(_.collection("user"))

  implicit def userHandler: BSONDocumentHandler[User] = Macros.handler[User]

  def findUserByUsernameAndPassword(username: String, password: String): Future[Option[User]] =
    userCollection.flatMap(_.find(document("username" -> username, "password" -> password)).one)

  def createUser(user: User): Future[WriteResult] = userCollection.flatMap(_.insert(user))

}
