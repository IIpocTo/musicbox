package musicbox

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, ExceptionHandler, RejectionHandler, Route}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import musicbox.routes.{AuthRouter, MusicboxRouter, UserRouter}
import musicbox.session.directives.csrf.CsrfDirectives.randomTokenCsrfProtection
import musicbox.session.directives.csrf.CsrfOptions.checkHeader
import musicbox.MusicboxSessionManager._
import musicbox.db.{MusicboxDao, UserDao}
import musicbox.service.{AuthService, MusicboxService, UserService}
import musicbox.swagger.SwaggerDocService
import com.softwaremill.macwire._

import scala.concurrent.ExecutionContext

class HttpRoute(implicit executionContext: ExecutionContext) {

  private lazy val userDao: UserDao = wire[UserDao]
  private lazy val musicboxDao: MusicboxDao = wire[MusicboxDao]

  private lazy val authService: AuthService = wire[AuthService]
  private lazy val authRouter: AuthRouter = wire[AuthRouter]

  private lazy val userService: UserService = wire[UserService]
  private lazy val userRouter: UserRouter = wire[UserRouter]

  private lazy val musicboxService: MusicboxService = wire[MusicboxService]
  private lazy val musicboxRouter: MusicboxRouter = wire[MusicboxRouter]

  val rejectionHandler: RejectionHandler =
    corsRejectionHandler.withFallback(RejectionHandler.default)

  val exceptionHandler = ExceptionHandler {
    case e: Exception => complete(StatusCodes.BadRequest -> e.getMessage)
  }

  val handleErrors: Directive[Unit] =
  handleRejections(rejectionHandler) &
  handleExceptions(exceptionHandler)


  def assets = pathPrefix("swagger") {
    getFromResourceDirectory("swagger") ~ pathSingleSlash(get(redirect("index.html", StatusCodes.PermanentRedirect))) }

  val routes: Route =
    handleErrors {
      cors() {
        handleErrors {
          randomTokenCsrfProtection(checkHeader) {
            pathPrefix("v1") {
              authRouter.route ~ userRouter.route ~ musicboxRouter.route ~
              path("healthcheck") {
                get {
                  complete("OK")
                }
              }
            }
          } ~ assets ~ SwaggerDocService.routes
        }
      }
    }

}
