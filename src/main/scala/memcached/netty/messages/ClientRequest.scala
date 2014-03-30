package memcached.netty.messages

sealed abstract class ClientRequest( val code : Int )

class SetRequest( val key : String, val value : Array[Byte], val flags : Int = 0, val expiration : Int = 0 )
  extends ClientRequest(Keys.Set)

class GetRequest( val key : String )
  extends ClientRequest(Keys.Get)

class DeleteRequest( val key : String )
  extends ClientRequest(Keys.Delete)