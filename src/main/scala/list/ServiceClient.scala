package list

case class User( id : Int, name : String, password : String )

case class ServiceResponse( status : Int, message : String )

class ServiceClient( val users : Map[Int,User]) {

  def userDetails(id : Int, password : String) : Either[ServiceResponse,User] = {

    users.get(id) match {
      case scala.Some(user) => if ( user.password == password ) {
        Right(user)
      } else {
        Left(ServiceResponse(401, "You can not view this user's details"))
      }
      case scala.None => Left(ServiceResponse(404, "User does not exist"))
    }

  }

}
