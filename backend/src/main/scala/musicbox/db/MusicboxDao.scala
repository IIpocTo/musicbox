package musicbox.db

import musicbox.db.Connection.musicboxDb
import musicbox.models.Models.{Album, Artist, Track}
import reactivemongo.api.Cursor
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentHandler, BSONObjectID, Macros, document}

import scala.concurrent.{ExecutionContext, Future}

class MusicboxDao(implicit ec: ExecutionContext) {

  private def artistCollection: Future[BSONCollection] = musicboxDb.map(_.collection("artists"))
  private def albumCollection: Future[BSONCollection] = musicboxDb.map(_.collection("albums"))
  private def trackCollection: Future[BSONCollection] = musicboxDb.map(_.collection("tracks"))

  implicit def artistHandler: BSONDocumentHandler[Artist] = Macros.handler[Artist]
  implicit def albumHandler: BSONDocumentHandler[Album] = Macros.handler[Album]
  implicit def trackHandler: BSONDocumentHandler[Track] = Macros.handler[Track]

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

  def findAlbumById(albumId: String): Future[Option[Album]] = {
    BSONObjectID.parse(albumId) map { objId =>
      albumCollection.flatMap(_.find(document("_id" -> objId)).one[Album])
    } getOrElse Future.successful(None)
  }

  def findAlbums(page: Int, limit: Int): Future[Vector[Album]] = {
    albumCollection.flatMap(
      _.find(BSONDocument())
          .skip((page - 1) * limit)
          .cursor[Album]()
          .collect[Vector](limit, Cursor.FailOnError[Vector[Album]]())
    )
  }

  def findTrackById(trackId: String): Future[Option[Track]] = {
    BSONObjectID.parse(trackId) map { objId =>
      trackCollection.flatMap(_.find(document("_id" -> objId)).one[Track])
    } getOrElse Future.successful(None)
  }

}
