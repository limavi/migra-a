package autenticacion.servicios

import java.security.MessageDigest
import akka.actor.ActorRef
import autenticacion.modelo.Auth


object AutServicios {

  def createToken( user: String ) = "gsdfgsdfgsdfgsdfgsdfg"//tokenGen.createToken( user )

  def validarUsuario( aut: Auth ): Option[ String ] = {
    if (true)  Some(createToken( aut.user ))
    else None
  }

  /*
  private def crearSesion( token: String, expiration: Long ): Option[ ActorRef ] = {
    println( "tokennnnnnn: " + token )
    val name = MessageDigest.getInstance( "MD5" ).digest( token.split( "\\." )( 2 ).getBytes ).map { b => String.format( "%02X", java.lang.Byte.valueOf( b ) ) }.mkString( "" ).toString
    log.info( "Creating user session. Expiration time: " + expiration + " minutes" )
    val session = sessions.get( name )
    session match {
      case Some( actor: ActorRef ) => None
      case None                    => Some( context.actorOf( SessionActor.props( expiration, name ) ) )
    }
  }
  */
}
