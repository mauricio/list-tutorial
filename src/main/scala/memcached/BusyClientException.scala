package memcached

/**
 * User: mauricio
 * Date: 3/29/14
 * Time: 12:12 AM
 */
class BusyClientException
  extends IllegalStateException("This client already has pending requests, you have to wait until they are finished")
