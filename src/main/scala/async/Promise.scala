package async

import scala.util.{Failure, Success, Try}

object Promise {

  def apply[T]() = new Promise[T]()

  def success[T]( value : T ) : Promise[T] = {
    val promise = Promise[T]()
    promise.success(value)
    promise
  }

}

class Promise[T] {

  @volatile private var result : Try[T] = null
  private val internalFuture = new DefaultFuture[T]()

  def future : Future[T] = internalFuture

  def isCompleted : Boolean = result != null

  def value : Try[T] = {
    if (this.isCompleted) {
      result
    } else {
      throw new IllegalStateException("this promise is not completed yet")
    }
  }

  def complete(result : Try[T]) : this.type = {
    if ( !this.tryComplete(result) ) {
      throw new IllegalStateException("promise already completed")
    }

    this
  }

  def tryComplete( result : Try[T] ) : Boolean = {
    if ( result == null ) {
      throw new IllegalArgumentException("result can't be null")
    }

    synchronized {
      if ( isCompleted ) {
        false
      } else {
        this.result = result
        this.internalFuture.complete(result)
        true
      }
    }
  }

  def success( value : T ) : this.type = complete(Success(value))
  def trySuccess( value : T ) : Boolean = tryComplete(Success(value))

  def failure(exception : Throwable) : this.type = complete(Failure(exception))
  def tryFailure(exception : Throwable) : Boolean = tryComplete(Failure(exception))

}
