package memcached

import memcached.netty.messages.ServerResponse

class CommandFailedException(val msg: ServerResponse)
  extends IllegalArgumentException(s"Command ${msg.command} failed with error ${msg.status}")
