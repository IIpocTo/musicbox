package musicbox

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import musicbox.routes.AuthRouter
import musicbox.session.directives.csrf.CsrfDirectives.randomTokenCsrfProtection
import musicbox.session.directives.csrf.CsrfOptions.checkHeader
import musicbox.MusicboxSessionManager._
//import com.softwaremill.macwire._
//import musicbox.db.UserDao
//import musicbox.routes.AuthRouter
//import musicbox.service.AuthService

import scala.concurrent.ExecutionContext

class HttpRoute(implicit executionContext: ExecutionContext) {

//  private val userDao = UserDao()
//  implicit private val authService: AuthService = wire[AuthService]
  private val authRouter = AuthRouter()

  val routes: Route =
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
