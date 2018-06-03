package musicbox.session.directives.csrf

import akka.http.scaladsl.server.{Directive0, Directive1}
import akka.http.scaladsl.server.Directives._

trait CsrfDirectives {

  def csrfTokenFromCookie[T](checkMode: CsrfCheckMode[T]): Directive1[Option[String]] =
    optionalCookie(checkMode.manager.config.csrfCookieConfig.name).map(_.map(_.value))

  def setNewCsrfToken[T](checkMode: CsrfCheckMode[T]): Directive0 =
    setCookie(checkMode.csrfManager.createCookie())

  def submittedCsrfToken[T](checkMode: CsrfCheckMode[T]): Directive1[String] = {
    headerValueByName(checkMode.manager.config.csrfSubmittedName).recover { rejections =>
      checkMode match {
        case c: CheckHeaderAndForm[T] =>
          import c.materializer
          formField(checkMode.manager.config.csrfSubmittedName)
        case _ => reject(rejections: _*)
      }
    }
  }

  def randomTokenCsrfProtection[T](checkMode: CsrfCheckMode[T]): Directive0 = {
    csrfTokenFromCookie(checkMode).flatMap {
      case Some(cookie) =>
        get.recover { _ =>
          submittedCsrfToken(checkMode).flatMap { submitted =>
            if (submitted == cookie) {
              pass
            } else {
              reject(checkMode.csrfManager.tokenInvalidRejection).toDirective[Unit]
            }
          }
        }
      case None =>
        (get & setNewCsrfToken(checkMode)).recover(_ => reject(checkMode.csrfManager.tokenInvalidRejection))
    }
  }

}

object CsrfDirectives extends CsrfDirectives
