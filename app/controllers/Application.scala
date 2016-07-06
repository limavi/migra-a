package controllers

import javax.inject.Inject

import autenticacion.modelo.Auth
import autenticacion.servicios.AutServicios
import play.api.libs.json._
import play.api.mvc._
import views.html

import scala.concurrent.Future

class Application @Inject()(webJarAssets: WebJarAssets) extends Controller {

  implicit val pacienteFormat = Json.format[Auth]

  def autenticar: Action[JsValue] = Action.async( BodyParsers.parse.json ) { request =>
    request.body.validate[ Auth ] match {
      case JsSuccess( auth, x ) =>
        AutServicios.validarUsuario(auth) match {
          case Some(token)=> Future.successful(Accepted(token))
          case None=> Future.successful( BadRequest( "Error en el request enviado a autenticar:  " + request.body.toString()) )
        }

      case err @ JsError( _ ) =>
        Future.successful( BadRequest( "Error en el request enviado a autenticar:  " + request.body.toString()) )
    }
  }

  def index = Action {Ok(html.index(webJarAssets))}

  def login = Action {Ok(html.login(webJarAssets))}


}
