package async

import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext
import scala.collection.mutable.ArrayBuffer
import scala.util.control.NonFatal

class DefaultFuture[T] extends Future[T] {

  class FutureCallback( val function : (Try[T]) => Any, val context : ExecutionContext )

  @volatile private var result : Try[T] = null
  private val callbacks = new ArrayBuffer[FutureCallback]()

  def isCompleted: Boolean = result != null

  def value: Option[Try[T]] = if (this.isCompleted) {
    Some(result)
  } else {
    None
  }

  def complete(value : Try[T]) {
    if (value == null) {
      throw new IllegalArgumentException("A future can't be completed with null")
    }

    synchronized {
      if ( !this.isCompleted ) {
        result = value
        fireCallbacks()
      }
    }
  }

  def flatMap[S](f: (T) => Future[S])(implicit executor: ExecutionContext): Future[S] = {
    val p = Promise[S]()
    onComplete {
      case Success(v) => try {
        f(v).onComplete(p.complete)
      } catch {
        case NonFatal(e) => p.failure(e)
      }
      case Failure(e) => p.failure(e)
    }
    p.future
  }

  def map[S](f: (T) => S)(implicit executor: ExecutionContext): Future[S] = {
    val p = Promise[S]()
    onComplete { v => p complete (v map f) }
    p.future
  }

  override def onComplete[U](f: (Try[T]) => U)(implicit executor: ExecutionContext): Unit = {
    val callback = new FutureCallback(f, executor)
    this.synchronized {
      if ( this.isCompleted ) {
        fireCallback(callback)
      } else {
        callbacks += callback
      }
    }
  }

  private def fireCallbacks() = {
    callbacks.foreach(fireCallback)
    callbacks.clear()
  }

  private def fireCallback( callback : FutureCallback) {
    callback.context.execute(new Runnable {
      def run() = callback.function(result)
    })
  }

}
