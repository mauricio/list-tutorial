package async

import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure, Try}

trait Future[T] {

  def isCompleted: Boolean

  def value: Option[Try[T]]

  def flatMap[S](f: (T) => Future[S])(implicit executor: ExecutionContext): Future[S]
  def map[S](f: (T) => S)(implicit executor: ExecutionContext): Future[S]

  def castTo[S](implicit executor: ExecutionContext): Future[S] = this.map(v => v.asInstanceOf[S])

  def foreach[U](f: (T) => U)(implicit executor: ExecutionContext): Unit = map(f)

  def onComplete[U](f: (Try[T]) => U)(implicit executor: ExecutionContext): Unit

  def onFailure[U](pf: PartialFunction[Throwable, U])(implicit executor: ExecutionContext) = onComplete {
    case Failure(e) => pf.apply(e)
    case _ =>
  }


  def onSuccess[U](pf: PartialFunction[T, U])(implicit executor: ExecutionContext): Unit = onComplete {
    case Success(v) => pf.apply(v)
    case _ =>
  }

}
