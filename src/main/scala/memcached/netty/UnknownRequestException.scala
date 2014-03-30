package memcached.netty

import memcached.netty.messages.ClientRequest

class UnknownRequestException(val request : ClientRequest )
  extends IllegalArgumentException(s"Don't know how to encode message ${request.code} - ${request}")
