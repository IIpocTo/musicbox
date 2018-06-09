package musicbox.swagger

import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.Info
import io.swagger.models.ExternalDocs
import io.swagger.models.auth.BasicAuthDefinition
import musicbox.routes.{AuthRouter, UserRouter, MusicboxRouter}

object SwaggerDocService extends SwaggerHttpService {
  override val apiClasses = Set(classOf[AuthRouter], classOf[MusicboxRouter], classOf[UserRouter])
  override val host = "127.0.0.1:9000"
  override val basePath = "/"
  override val info = Info(version = "1.0")
  override val externalDocs = Some(new ExternalDocs("Core Docs", "http://acme.com/docs"))
  override val securitySchemeDefinitions = Map("basicAuth" -> new BasicAuthDefinition())
}