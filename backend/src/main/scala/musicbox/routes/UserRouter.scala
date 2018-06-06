package musicbox.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive1, Route}
import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
import com.typesafe.scalalogging.StrictLogging
import musicbox.MusicboxSessionManager._
import musicbox.SessionData
import musicbox.service.UserService
import musicbox.session.SessionOptions.{refreshable, usingHeaders}
import musicbox.session.directives.SessionDirectives.requiredSession

import scala.concurrent.ExecutionContext

class UserRouter(service: UserService)(implicit executionContext: ExecutionContext)
    extends StrictLogging {

  val musicboxRequiredSession: Directive1[SessionData] = requiredSession(refreshable, usingHeaders)

  val route: Route =
    pathPrefix("user") {
      path("profile" / "me") {
        get {
          musicboxRequiredSession { session =>
            val optUser = service.getUser(session.userId)
            onSuccess(optUser) {
              case Some(u) =>
                logger.info(s"Requested user $u")
                complete(u)
              case _ => complete(StatusCodes.Conflict, "No such user")
            }
          }
        }
      }
    }
}
