package memcached.netty.messages

object Keys {
  // magic
  val RequestKey = 0x80
  val ResponseKey = 0x81

  // requests

  final val Get = 0x00
  final val Set = 0x01
  final val Delete = 0x04
}
