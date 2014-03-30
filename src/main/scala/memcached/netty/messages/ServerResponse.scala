package memcached.netty.messages

object ServerResponse {
  final val Ok = 0x0000
  final val NotFound = 0x0001
  final val Exists = 0x0002
  final val ItemNotStored = 0x0005

  final val ValueTooLarge = 0x0003
  final val InvalidArguments = 0x0004
  final val IncrementDecrementNonNumeric = 0x0006
  final val Unknown = 0x0081
  final val OutOfMemory = 0x0082
}

sealed abstract class ServerResponse(
  val command: Int,
  val status: Int,
  val opaque: Int,
  val cas: Long) {

  import ServerResponse._

  def isError: Boolean = status match {
    case Ok | NotFound | Exists | ItemNotStored => false
    case _ => true
  }

}

class StatusResponse(command: Int, status: Int, opaque: Int, cas: Long, val body: Option[String] = None)
  extends ServerResponse(command, status, opaque, cas)

class GetResponse(val value: Option[Array[Byte]], status: Int, val flags: Int, opaque: Int, cas: Long, body : Option[String] = None)
  extends StatusResponse(Keys.Get, status, opaque, cas, body)
