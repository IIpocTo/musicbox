package musicbox.db

import musicbox.db.Connection.musicboxDb
import musicbox.models.Models.User
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocumentHandler, BSONObjectID, Macros, document}

import scala.concurrent.{ExecutionContext, Future}

case class UserDao()(implicit ec: ExecutionContext) {

  private def userCollection: Future[BSONCollection] = musicboxDb.map(_.collection("user"))

  implicit def userHandler: BSONDocumentHandler[User] = Macros.handler[User]

  def findUserByUsernameAndPassword(username: String, password: String): Future[Option[User]] =
    userCollection.flatMap(_.find(document("username" -> username, "password" -> password)).one)

  def findById(userId: String): Future[Option[User]] = {
    BSONObjectID.parse(userId) map { objId =>
      userCollection.flatMap(_.find(document("_id" -> objId)).one[User])
    } getOrElse Future.successful(None)
  }

  def insertUser(user: User): Future[Boolean] =
    userCollection.flatMap(_.insert(user)).map(wr => wr.ok || wr.n == 1)

}
