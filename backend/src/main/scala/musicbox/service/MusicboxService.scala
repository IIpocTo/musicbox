package musicbox.service

import java.time.{Instant, ZoneId}

import musicbox.db.MusicboxDao
import musicbox.models.Models._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class MusicboxService(musicboxDao: MusicboxDao)(implicit executionContext: ExecutionContext) {

  private def artistToResponse(artist: Artist): ArtistResponse =
    ArtistResponse(
      artist.id.stringify,
      artist.name,
      artist.genres,
      artist.image,
      artist.albums.map(_.id.stringify)
    )

  private def trackToResponse(track: Track): TrackResponse =
    TrackResponse(
      track.id.stringify,
      track.name,
      track.duration,
      track.content
    )

  private def albumToResponse(album: Album, tracks: Seq[Option[Track]]): AlbumResponse =
    AlbumResponse(
      album.id.stringify,
      album.artists.map(_.id.stringify),
      album.name,
      album.image,
      Instant.ofEpochMilli(album.releaseDate.toLong).atZone(ZoneId.systemDefault()).toLocalDate,
      tracks.flatten.map(trackToResponse)
    )

  private def albumToResponseWithoutTracks(album: Album): AlbumResponseWithoutTracks =
    AlbumResponseWithoutTracks(
      album.id.stringify,
      album.artists.map(_.id.stringify),
      album.name,
      album.image,
      Instant.ofEpochMilli(album.releaseDate.toLong).atZone(ZoneId.systemDefault()).toLocalDate
    )

  def futureToFutureTry[T](fut: Future[T]): Future[Try[T]] =
    fut.map(Success(_)).recover({ case x => Failure(x) })

  def getArtist(artistId: String): Future[Option[ArtistResponse]] =
    musicboxDao.findArtistById(artistId).map(_.map(artistToResponse))

  def getArtists(page: Int, limit: Int): Future[Vector[ArtistResponse]] =
    if (page <= 0 || limit <= 0) Future.successful(Vector.empty)
    else musicboxDao.findArtists(page, limit).map(_.map(artistToResponse))

  def getAlbums(page: Int, limit: Int): Future[Vector[AlbumResponseWithoutTracks]] =
    if (page <= 0 || limit <= 0) Future.successful(Vector.empty)
    else musicboxDao.findAlbums(page, limit).map(_.map(albumToResponseWithoutTracks))

  def getAlbum(albumId: String): Future[Option[AlbumResponse]] = {
    musicboxDao.findAlbumById(albumId).flatMap {
      case Some(album) =>
        val seqTracks = album.tracks
          .map(_.id.stringify)
          .map(id => musicboxDao.findTrackById(id))
        Future.sequence(seqTracks).map(Option(_)).map(_.map(albumToResponse(album, _)))
      case None => Future.successful(None)
    }
  }

}
