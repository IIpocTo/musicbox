package musicbox.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Directive1, Route}
import com.typesafe.scalalogging.StrictLogging
import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
import musicbox.session.SessionOptions._
import musicbox.SessionData
import musicbox.MusicboxSessionManager._
import musicbox.models.Models.{LoginRequest, RegisterRequest}
import musicbox.service.AuthService
import musicbox.session.directives.SessionDirectives._
import io.swagger.annotations._
import scala.concurrent.ExecutionContext

@Api(value = "/auth", description = "Authorization and authentication")
case class AuthRouter(service: AuthService)(implicit executionContext: ExecutionContext)
    extends StrictLogging {

  def musicboxSetSession(value: SessionData): Directive0 =
    setSession(refreshable, usingHeaders, value)

  val musicboxRequiredSession: Directive1[SessionData] = requiredSession(refreshable, usingHeaders)
  val musicboxInvalidateSession: Directive0 = invalidateSession(refreshable, usingHeaders)

  @ApiOperation(httpMethod = "POST", value = "Register new user", response = classOf[Unit])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "username", required = true, dataType = "string", value = "Username"),
    new ApiImplicitParam(name = "password", required = true, dataType = "string", value = "Password"),
    new ApiImplicitParam(name = "email", required = true, dataType = "string", value = "Email"),
    new ApiImplicitParam(name = "phone", required = true, dataType = "string", value = "Phone")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 409, message = "Error while registering user!"),
    new ApiResponse(code = 201, message = "")))
  def registerRoute = path("register") {
      post {
        entity(as[RegisterRequest]) { rr =>
          logger.info(s"Register with $rr")
          onSuccess(service.register(rr)) { inserted =>
            if (inserted) complete(StatusCodes.Created)
            else complete(StatusCodes.Conflict, "Error while registering user!")
          }
        }
      }
  }

  @ApiOperation(httpMethod = "POST", value = "Login user")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "username", required = true, dataType = "string", value = "Username"),
    new ApiImplicitParam(name = "password", required = true, dataType = "string", value = "Password"),
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 409, message = "Wrong login data!"),
    new ApiResponse(code = 200, message = "")))
  def loginRoute = path("login") {
    post {
      entity(as[LoginRequest]) { lr =>
        logger.info(s"Logging with $lr")
        onSuccess(service.login(lr)) {
          case Some(id) =>
            musicboxSetSession(SessionData(id)) { ctx =>
              ctx.complete(StatusCodes.OK)
            }
          case None => complete(StatusCodes.Conflict, "Wrong login data!")
        }
      }
    }
  }

  val route: Route =
    pathPrefix("auth") {
      registerRoute  ~ loginRoute ~
      path("logout") {
        post {
          musicboxRequiredSession { session =>
            musicboxInvalidateSession { ctx =>
              logger.info(s"Logging out $session")
              ctx.complete(StatusCodes.OK, "ok")
            }
          }
        }
      } ~
      path("refresh") {
        get {
          musicboxRequiredSession { _ => ctx =>
            logger.info(s"Refresh with headers: ${ctx.request.headers}")
            ctx.complete(StatusCodes.OK)
          }
        }
      }
    }
}
