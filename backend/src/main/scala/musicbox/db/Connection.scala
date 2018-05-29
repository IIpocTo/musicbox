package musicbox.db

import com.typesafe.config.ConfigFactory
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

object Connection {

  import ExecutionContext.Implicits.global

  private val mongoUri: String = ConfigFactory.load().getString("mongo.path")
  private val driver: MongoDriver = MongoDriver()
  private val parsedUri: Try[MongoConnection.ParsedURI] = MongoConnection.parseURI(mongoUri)
  private val connection: Try[MongoConnection] = parsedUri.map(driver.connection)

  private val futureConnection: Future[MongoConnection] = Future.fromTry(connection)

  def musicboxDb: Future[DefaultDB] = futureConnection.flatMap(_.database("musicbox"))

}
