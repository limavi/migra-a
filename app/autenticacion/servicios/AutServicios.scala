package autenticacion.servicios

import autenticacion.modelo.Auth
import java.util.{Date, UUID}
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.{JWSAlgorithm, JWSHeader}
import com.nimbusds.jwt.{JWTClaimsSet, SignedJWT}

import scala.collection.JavaConversions._
import scala.collection.mutable.Map

object AutServicios {
  private val ISSUER = "MigranaApp"
  private val SIGNING_KEY = "fasdSsdfalsdkoliieñs9423o4ilñaslñ4ññslkAAÑk6sdlkfaiseo7nalksdmc8m9lsnklfnlaiengldyhsltohsnf0lsinnoi"

  def validarUsuario( aut: Auth ): Option[ String ] = {
    if (true)  //aqui debemos consultar en la bd
      Some(createToken( aut.user ))
    else None
  }

  def createToken(user: String): String = {
    val issued: Long = System.currentTimeMillis()
    val claimsSet: JWTClaimsSet = new JWTClaimsSet()
    claimsSet.setIssueTime(new Date(issued))
    claimsSet.setJWTID(UUID.randomUUID.toString)
    claimsSet.setIssuer(ISSUER)
    val customData = Map[String, String](
      "user" -> user,
      "rol" -> "paciente"
    )
    claimsSet.setCustomClaims(customData)
    val signedJWT: SignedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet)
    val signer: MACSigner = new MACSigner(SIGNING_KEY)
    signedJWT.sign(signer)
    signedJWT.serialize() // este es el Token
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
