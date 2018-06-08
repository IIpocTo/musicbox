package musicbox.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.StrictLogging
import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
import musicbox.MusicboxSessionManager._
import musicbox.models.Models.Artist
import musicbox.service.MusicboxService

import scala.concurrent.ExecutionContext

class MusicboxRouter(service: MusicboxService)(implicit executionContext: ExecutionContext)
    extends StrictLogging {

  val route: Route =
  path("artist") {
    get {
      parameters("aid".?) {
        case Some(aid) =>
          logger.info(s"Requested artist with aid = $aid")
          onSuccess(service.getArtist(aid)) {
            case Some(artist) =>
              logger.info(s"Returning artist: $artist")
              complete(artist)
            case None =>
              logger.warn(s"No artist with id = $aid")
              complete(StatusCodes.Conflict, "No artist for such aid")
          }
        case None => complete(StatusCodes.BadRequest, "aid query parameter missing")
      }
    }
  } ~
  path("artists") {
    get {
      onSuccess(service.getArtists) { artists =>
        complete(artists)
      }
    }
  }

}
