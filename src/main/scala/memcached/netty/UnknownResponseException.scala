package memcached.netty

class UnknownResponseException(val commandCode: Int)
  extends IllegalArgumentException(s"Don't know how to decode message type ${commandCode}")
