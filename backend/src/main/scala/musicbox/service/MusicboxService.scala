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

  def getArtists: Future[Vector[ArtistResponse]] =
    musicboxDao.findArtists().map(_.map(artistToResponse))

}
