package musicbox.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._
import musicbox.service.auth.AuthService

import scala.concurrent.ExecutionContext

case class SignInData(login: String, password: String)
case class SignUpData(username: String, password: String, email: String)

class AuthRouter(authService: AuthService)(implicit executionContext: ExecutionContext)
    extends FailFastCirceSupport {

  import StatusCodes._
  import authService._

  val route: Route =
    pathPrefix("auth") {
      path("signIn") {
        pathEndOrSingleSlash {
          post {
            entity(as[SignInData]) { signInData =>
              println(signInData)
              complete(
                signIn(signInData).map {
                  case Some(token) => OK         -> token.asJson
                  case None        => BadRequest -> None.asJson
                }
              )
            }
          }
        }
      } ~
      path("signUp") {
        pathEndOrSingleSlash {
          post {
            entity(as[SignUpData]) { signUpData =>
              complete(Created -> signUp(signUpData))
            }
          }
        }
      }
    }

}
