package memcached.netty

import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.channel.ChannelHandlerContext
import io.netty.buffer.ByteBuf
import java.util
import memcached.netty.messages._
import scala.annotation.switch
import io.netty.util.CharsetUtil

class MemcachedDecoder extends ByteToMessageDecoder {

  def decode(ctx: ChannelHandlerContext, in: ByteBuf, out: util.List[AnyRef]) {

    if (in.readableBytes() >= 24) {
      in.markReaderIndex()

      in.readByte() // magic number
      val commandCode = in.readByte()
      val keyLength = in.readUnsignedShort()
      val extrasLength = in.readUnsignedByte()
      val dataType = in.readByte()
      val status = in.readShort()
      val bodyLength = in.readUnsignedInt()
      val opaque = in.readInt()
      val cas = in.readLong()

      if (in.readableBytes() >= bodyLength) {
        (commandCode: @switch) match {
          case Keys.Get => {
            val flags = if ( extrasLength > 0 ) {
              in.readInt()
            } else {
              0
            }

            val bytes = new Array[Byte](bodyLength.toInt - extrasLength)
            in.readBytes(bytes)

            val value = if (status == ServerResponse.Ok) {
              Some(bytes) -> None
            } else {
              None -> Some(new String(bytes, CharsetUtil.US_ASCII))
            }

            out.add(new GetResponse(value._1, status, flags, opaque, cas, value._2))
          }
          case _ if extrasLength == 0 => {
            val body = if (bodyLength > 0) {
              Some(in.toString(CharsetUtil.US_ASCII))
            } else {
              None
            }
            in.readerIndex((in.readerIndex() + bodyLength).toInt)
            out.add(new StatusResponse(commandCode, status, opaque, cas, body))
          }
          case _ => throw new UnknownResponseException(commandCode)
        }

      } else {
        in.resetReaderIndex()
      }
    }

  }

}
