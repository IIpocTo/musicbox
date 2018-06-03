package musicbox.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Directive1, Route}
import musicbox.session.SessionOptions._
import com.typesafe.scalalogging.StrictLogging
import musicbox.SessionData
import musicbox.MusicboxSessionManager._
import musicbox.session.directives.SessionDirectives._

import scala.concurrent.ExecutionContext

case class SignInData(username: String, password: String)
case class SignUpData(username: String, password: String, email: String, phone: String)

case class AuthRouter()(implicit executionContext: ExecutionContext) extends StrictLogging {

  def musicboxSetSession(value: SessionData): Directive0 =
    setSession(refreshable, usingCookies, value)

  val musicboxRequiredSession: Directive1[SessionData] = requiredSession(refreshable, usingCookies)
  val musicboxInvalidateSession: Directive0 = invalidateSession(refreshable, usingCookies)

  val route: Route =
  path("login") {
    post {
      entity(as[String]) { body =>
        logger.info(s"Logging in $body")
        musicboxSetSession(SessionData(body)) { ctx =>
          ctx.complete(StatusCodes.OK, "ok")
        }
      }
    }
  } ~
  path("logout") {
    post {
      musicboxRequiredSession { session =>
        musicboxInvalidateSession { ctx =>
          logger.info(s"Logging out $session")
          ctx.complete(StatusCodes.OK, "ok")
        }
      }
    }
  }

}
