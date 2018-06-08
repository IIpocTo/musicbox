package musicbox.db

import musicbox.db.Connection.musicboxDb
import musicbox.models.Models.Artist
import reactivemongo.api.{Cursor, QueryOpts}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentHandler, BSONObjectID, Macros, document}

import scala.concurrent.{ExecutionContext, Future}

class MusicboxDao(implicit ec: ExecutionContext) {

  private def artistCollection: Future[BSONCollection] = musicboxDb.map(_.collection("artists"))
  private def albumCollection: Future[BSONCollection] = musicboxDb.map(_.collection("albums"))
  private def trackCollection: Future[BSONCollection] = musicboxDb.map(_.collection("tracks"))

  implicit def artistHandler: BSONDocumentHandler[Artist] = Macros.handler[Artist]

  def findArtistById(artistId: String): Future[Option[Artist]] = {
    BSONObjectID.parse(artistId) map { objId =>
      artistCollection.flatMap(_.find(document("_id" -> objId)).one[Artist])
    } getOrElse Future.successful(None)
  }

  def findArtists(page: Int, limit: Int): Future[Vector[Artist]] = {
    artistCollection.flatMap(
      _.find(BSONDocument())
        .sort(document("popularity" -> -1))
        .skip((page - 1) * limit)
        .cursor[Artist]()
        .collect[Vector](limit, Cursor.FailOnError[Vector[Artist]]())
    )
  }

}