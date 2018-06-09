package musicbox.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.StrictLogging
import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
import musicbox.MusicboxSessionManager._
import musicbox.service.MusicboxService

import scala.concurrent.ExecutionContext

class MusicboxRouter(service: MusicboxService)(implicit executionContext: ExecutionContext)
    extends StrictLogging {

  val route: Route =
  path("artist") {
    get {
      parameters("id".?) {
        case Some(id) =>
          logger.info(s"Requested artist with id = $id")
          onSuccess(service.getArtist(id)) {
            case Some(artist) =>
              logger.info(s"Returning artist: $artist")
              complete(artist)
            case None =>
              logger.warn(s"No artist with id = $id")
              complete(StatusCodes.Conflict, "No artist for such id")
          }
        case None => complete(StatusCodes.BadRequest, "aid query parameter is missing")
      }
    }
  } ~
  path("artists") {
    get {
      parameters("page".as[Int].?, "limit".as[Int].?) { (page, limit) =>
        page match {
          case Some(p) =>
            limit match {
              case Some(l) =>
                logger.info(s"Requested artists with params: page = $page and limit = $limit")
                onSuccess(service.getArtists(p, l)) { artists =>
                  complete(artists)
                }
              case None => complete(StatusCodes.BadRequest, "page query parameter is missing")
            }
          case None => complete(StatusCodes.BadRequest, "limit query parameter is missing")
        }
      }
    }
  } ~
    path("album") {
      get {
        parameters("id".?) {
          case Some(id) =>
            logger.info(s"Requested album with id = $id")
            onSuccess(service.getAlbum(id)) {
              case Some(album) =>
                logger.info(s"Returning album: $album")
                complete(album)
              case None =>
                logger.warn(s"No artist with id = $id")
                complete(StatusCodes.Conflict, "No artist for such id")
            }
          case None => complete(StatusCodes.BadRequest, "id query parameter is missing")
        }
      }
    } ~
    path("albums") {
      get {
        parameters("page".as[Int].?, "limit".as[Int].?) { (page, limit) =>
          page match {
            case Some(p) =>
              limit match {
                case Some(l) =>
                  logger.info(s"Requested artists with params: page = $page and limit = $limit")
                  onSuccess(service.getAlbums(p, l)) { albums =>
                    complete(albums)
                  }
                case None => complete(StatusCodes.BadRequest, "page query parameter is missing")
              }
            case None => complete(StatusCodes.BadRequest, "limit query parameter is missing")
          }
        }
      }
    }
}
