package memcached

import org.specs2.mutable.Specification
import async.Future
import memcached.netty.messages.ServerResponse
import io.netty.util.CharsetUtil
import java.util.UUID

class ClientSpecification extends Specification {

  def toBytes(value: String): Array[Byte] = value.getBytes(CharsetUtil.US_ASCII)

  def fromBytes(bytes: Array[Byte]): String = new String(bytes, CharsetUtil.US_ASCII)

  def withClient[T](f: Client => T): T = {
    val client = new NettyClient()
    await(client.connect())
    try {
      f(client)
    } finally {
      await(client.close())
    }
  }

  def await[T](future: Future[T], seconds: Int = 3): T = {
    var count = 0
    while (!future.isCompleted && count <= seconds) {
      Thread.sleep(1000)
      count += 1
    }

    if (future.isCompleted) {
      future.value.get.get
    } else {
      throw new IllegalStateException(s"Trying to access the future did timeout after ${seconds} seconds")
    }
  }

  "client" should {

    "set a value and get it back correctly" in {
      withClient {
        client =>
          val unique = UUID.randomUUID().toString
          val key = s"hello-${unique}"
          val value = s"hello-world-${unique}"

          val result = await(client.set(key, toBytes(value)))
          result.isError must beFalse
          result.status === ServerResponse.Ok

          val response = await(client.get(key))
          fromBytes(response.value.get) === value
      }

    }

    "set a value twice and get it back twice correctly" in {
      withClient {
        client =>
          val unique = UUID.randomUUID().toString
          val key = s"hello-${unique}"
          val value = s"hello-world-${unique}"
          val otherValue = s"hello-world-again-${unique}"

          await(client.set(key, toBytes(value)))

          val response = await(client.get(key))
          fromBytes(response.value.get) === value

          await(client.set(key, toBytes(otherValue)))

          val otherResponse = await(client.get(key))
          fromBytes(otherResponse.value.get) === otherValue
      }

    }

    "not get anything if the key does not exist" in {

      withClient {
        client =>
          val result = await(client.get("Hello"))
          result.isError must beFalse
          result.status === ServerResponse.NotFound

          val otherResult = await(client.get("World"))
          otherResult.isError must beFalse
          otherResult.status === ServerResponse.NotFound
      }

    }

    "should delete an item if it is there" in {
      withClient {
        client =>
          val unique = UUID.randomUUID().toString
          val key = s"hello-${unique}"
          val value = s"hello-world-${unique}"

          val result = await(client.set(key, toBytes(value)))
          result.isError must beFalse
          result.status === ServerResponse.Ok

          val deleteResponse = await(client.delete(key))
          deleteResponse.status === ServerResponse.Ok

          val response = await(client.get(key))
          response.status === ServerResponse.NotFound
      }
    }

    "not delete an item if it does not exist" in {
      withClient {
        client =>
          val result = await(client.delete("Hello"))
          result.status === ServerResponse.NotFound
      }
    }

  }

}
