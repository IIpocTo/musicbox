package musicbox

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, ExceptionHandler, RejectionHandler, Route}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import musicbox.routes.{AuthRouter, UserRouter}
import musicbox.session.directives.csrf.CsrfDirectives.randomTokenCsrfProtection
import musicbox.session.directives.csrf.CsrfOptions.checkHeader
import musicbox.MusicboxSessionManager._
import musicbox.db.UserDao
import musicbox.service.{AuthService, UserService}
import com.softwaremill.macwire._

import scala.concurrent.ExecutionContext

class HttpRoute(implicit executionContext: ExecutionContext) {

  private lazy val userDao: UserDao = wire[UserDao]

  private lazy val authService: AuthService = wire[AuthService]
  private lazy val authRouter: AuthRouter = wire[AuthRouter]

  private lazy val userService: UserService = wire[UserService]
  private lazy val userRouter: UserRouter = wire[UserRouter]

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
              authRouter.route ~ userRouter.route ~
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
