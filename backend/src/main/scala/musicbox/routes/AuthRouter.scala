package musicbox.routes

//import akka.http.scaladsl.model.StatusCodes
//import akka.http.scaladsl.model.headers.RawHeader
//import akka.http.scaladsl.server.Directives._
//import akka.http.scaladsl.server.{Directive1, Route}
//import authentikat.jwt.{JsonWebToken, JwtHeader}
//import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
//import io.circe.generic.auto._
//import musicbox.service.AuthService
//
//import scala.concurrent.ExecutionContext
//import scala.util.Failure
//
//case class SignInData(username: String, password: String)
//case class SignUpData(username: String, password: String, email: String, phone: String)
//
//case class AuthRouter(authService: AuthService)(implicit executionContext: ExecutionContext)
//    extends FailFastCirceSupport {
//
//  import StatusCodes._
//  import authService._
//
//  val route: Route =
//    pathPrefix("auth") {
//      path("signIn") {
//        pathEndOrSingleSlash {
//          post {
//            entity(as[SignInData]) { siData =>
//              val claims = for {
//                user <- signIn(siData)
//              } yield setClaims(user.username)
//              claims onComplete {
//                case scala.util.Success(claim) =>
//                  respondWithHeader(
//                    RawHeader(
//                      "Access-Token",
//                      JsonWebToken(JwtHeader("HS256"), claim, authService.provideSecretKey)
//                    )
//                  )
//                  complete(StatusCodes.OK)
//                case Failure(_) => complete(StatusCodes.Unauthorized)
//              }
//            }
//          }
//        }
//      } ~
//      path("signUp") {
//        pathEndOrSingleSlash {
//          post {
//            entity(as[SignUpData]) { signUpData =>
//              complete(Created -> signUp(signUpData))
//            }
//          }
//        }
//      }
//    }
//
//}
