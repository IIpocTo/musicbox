package musicbox

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import musicbox.routes.AuthRouter
import musicbox.service.auth.AuthService

import scala.concurrent.ExecutionContext

class HttpRoute(authService: AuthService)(implicit executionContext: ExecutionContext) {

  private val authRouter = new AuthRouter(authService)

  val routes: Route =
    pathPrefix("v1") {
      authRouter.route ~
      path("healthcheck") {
        get {
          complete("OK")
        }
      }
    }
}
