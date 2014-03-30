package memcached

import async.Future
import memcached.netty.messages.{StatusResponse, GetResponse}

object Client {
  def apply( host: String = "localhost", port: Int = 11211 ) : Client =
    new NettyClient(host, port)
}

trait Client {

  def set(key: String, bytes: Array[Byte], flags: Int = 0, expiration: Int = 0): Future[StatusResponse]
  def get(key: String): Future[GetResponse]
  def delete(key: String) : Future[StatusResponse]
  def connect(): Future[Client]
  def close(): Future[Client]

}
