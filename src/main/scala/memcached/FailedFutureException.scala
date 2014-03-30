package memcached

/**
 * User: mauricio
 * Date: 3/30/14
 * Time: 12:26 PM
 */
class FailedFutureException( val channelFuture: ChannelFuture ) extends IllegalStateException
