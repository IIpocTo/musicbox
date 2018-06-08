package musicbox.db

import musicbox.db.Connection.musicboxDb
import musicbox.models.Models.Artist
import reactivemongo.api.Cursor
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{document, BSONDocument, BSONDocumentHandler, BSONObjectID, Macros}

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

  def findArtists(): Future[Vector[Artist]] = {
    artistCollection.flatMap(
      _.find(BSONDocument())
        .sort(document("popularity" -> -1))
        .cursor[Artist]()
        .collect[Vector](50, Cursor.FailOnError[Vector[Artist]]())
    )
  }

}
