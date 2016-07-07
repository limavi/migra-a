package controllers

import javax.inject.Inject

import autenticacion.modelo.Auth
import autenticacion.servicios.AutServicios
import migrana.modelo.{Episodio, Paciente,Repository}
import migrana.services.migranaServices
import play.api.libs.json._
import play.api.mvc._
import views.html

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class Application @Inject()(webJarAssets: WebJarAssets) extends Controller {

  implicit val authFormat = Json.format[Auth]

  import play.api.mvc._
  import play.api.libs.json._

  implicit val episodioFormat = Json.format[Episodio]
  implicit val pacienteFormat = Json.format[Paciente]

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

  def consultarEpisodios(id: Option[ Long ]) = Action.async { implicit request =>
    val res =migranaServices.getEpisodios(id) map { episodio =>
      Ok( Json.toJson( episodio ) )
    }
    res.map( _.withHeaders( ( ACCESS_CONTROL_ALLOW_ORIGIN, "*" ), ( CONTENT_TYPE, "application/hal+json" ) ) )
  }

  def consultarEpisodiosPorPaciente(TipoDocumento: String, NumeroDocumento: Long)= Action.async { implicit request =>
    val res =migranaServices.getEpisodiosPorPaciente(TipoDocumento,NumeroDocumento) map { episodiosPorPaciente =>
      Ok( Json.toJson( episodiosPorPaciente ) )
    }
    res.map( _.withHeaders( ( ACCESS_CONTROL_ALLOW_ORIGIN, "*" ), ( CONTENT_TYPE, "application/hal+json" ) ) )
  }

  def consultarPacientes(TipoDocumento:Option[ String ],  NumeroDocumento: Option[Long ]): Action[AnyContent] = Action.async { implicit request =>
    val res =migranaServices.getPacientes(TipoDocumento,NumeroDocumento ) map { pacientes =>
      Ok( Json.toJson( pacientes ) )
    }
    res.map( _.withHeaders( ( ACCESS_CONTROL_ALLOW_ORIGIN, "*" ), ( CONTENT_TYPE, "application/hal+json" ) ) )
  }

  def agregarEpisodio= Action.async { implicit request =>
    request.body.asJson.map { json =>
      json.validate[Episodio].map{
        case (episodio) => {
          Repository.addEpisodio(episodio).map(resultado => {
            //migranaServices.generarAlertaPorMuchosDolores(episodio.IdPaciente)
            Ok("Episodio agregado satisfactoriamente")
          })
        }
      }.recoverTotal{
        e => Future(BadRequest("Existe un error en el JSON: "+ JsError.toFlatJson(e)))
      }
    }.getOrElse {
      Future(BadRequest("Se esperaba un json para ejecutar el POST"))
    }
  }

  def sincronizarEpisodios = Action { request =>
    request.body.asJson.map { json =>
      json.validate[List[Episodio]].map {
        case (listEpisodios) => {
          migranaServices.addListEpisodios(listEpisodios).map(resultado => println(resultado))
          Ok("Episodios sincronizados satisfactoriamente")
        }
      }.recoverTotal {
        e => BadRequest("Existe un error en el JSON: " + JsError.toFlatJson(e))
      }
    }.getOrElse {
      BadRequest("Se esperaba un json para ejecutar el POST")
    }
  }
}
