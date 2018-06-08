package musicbox.service

import musicbox.db.MusicboxDao
import musicbox.models.Models.{Artist, ArtistResponse}

import scala.concurrent.{ExecutionContext, Future}

class MusicboxService(musicboxDao: MusicboxDao)(implicit executionContext: ExecutionContext) {

  private def artistToResponse(artist: Artist): ArtistResponse =
    ArtistResponse(
      artist.id.stringify,
      artist.name,
      artist.genres,
      artist.image,
      artist.albums.map(_.id.stringify)
    )

  def getArtist(artistId: String): Future[Option[ArtistResponse]] =
    musicboxDao.findArtistById(artistId).map(_.map(artistToResponse))

  def getArtists(page: Int, limit: Int): Future[Vector[ArtistResponse]] =
    if (page <= 0 || limit <= 0) Future.successful(Vector.empty)
    else musicboxDao.findArtists(page, limit).map(_.map(artistToResponse))
}
