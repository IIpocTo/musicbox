package musicbox.db

import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}
import reactivemongo.bson.{BSONDocumentWriter, Macros}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

case class User(username: String, password: String, email: String)

object Connection {

  import ExecutionContext.Implicits.global

  private val mongoUri: String = "mongodb://localhost:27017/musicbox?authMode=scram-sha1"
  private val driver: MongoDriver = MongoDriver()
  private val parsedUri: Try[MongoConnection.ParsedURI] = MongoConnection.parseURI(mongoUri)
  private val connection: Try[MongoConnection] = parsedUri.map(driver.connection)

  private val futureConnection: Future[MongoConnection] = Future.fromTry(connection)

  def musicboxDb: Future[DefaultDB] = futureConnection.flatMap(_.database("musicbox"))
  def userCollection: Future[BSONCollection] = musicboxDb.map(_.collection("user"))

  implicit def userWriter: BSONDocumentWriter[User] = Macros.writer[User]

  def createUser(user: User): Future[WriteResult] = userCollection.flatMap(_.insert(user))

}
