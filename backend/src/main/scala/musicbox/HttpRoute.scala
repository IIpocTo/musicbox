package musicbox

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, ExceptionHandler, RejectionHandler, Route}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import musicbox.routes.AuthRouter
import musicbox.session.directives.csrf.CsrfDirectives.randomTokenCsrfProtection
import musicbox.session.directives.csrf.CsrfOptions.checkHeader
import musicbox.MusicboxSessionManager._
import musicbox.db.UserDao
import musicbox.service.AuthService
import com.softwaremill.macwire._

import scala.concurrent.ExecutionContext

class HttpRoute(implicit executionContext: ExecutionContext) {

  implicit private val userDao: UserDao = wire[UserDao]
  implicit private val authService: AuthService = wire[AuthService]
  implicit private val authRouter: AuthRouter = wire[AuthRouter]

  val rejectionHandler: RejectionHandler =
    corsRejectionHandler.withFallback(RejectionHandler.default)

  val exceptionHandler = ExceptionHandler {
    case e: Exception => complete(StatusCodes.BadRequest -> e.getMessage)
  }

  val handleErrors: Directive[Unit] =
  handleRejections(rejectionHandler) &
  handleExceptions(exceptionHandler)

  val routes: Route =
    handleErrors {
      cors() {
        handleErrors {
          randomTokenCsrfProtection(checkHeader) {
            pathPrefix("v1") {
              authRouter.route ~
              path("healthcheck") {
                get {
                  complete("OK")
                }
              }
            }
          }
        }
      }
    }

}
