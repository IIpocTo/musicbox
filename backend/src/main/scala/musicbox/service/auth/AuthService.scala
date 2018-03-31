package musicbox.service.auth

import musicbox.db.{Connection, User}
import musicbox.routes.{SignInData, SignUpData}

import scala.concurrent.{ExecutionContext, Future}

class AuthService(secretKey: String)(implicit executionContext: ExecutionContext) {

  def signIn(signInData: SignInData): Future[Option[String]] = {
    Future.unit.map(_ => Some("token"))
  }

  def signUp(signUpData: SignUpData): Future[String] = {
    Connection
      .createUser(User(signUpData.username, signUpData.password, signUpData.email))
      .map(wr => if (wr.ok) "success" else "failure")
  }

}
