package list

import scala.util.control.NonFatal

object Try {
  def apply[T](f: => T): Try[T] =
    try {
      Success(f)
    } catch {
      case NonFatal(e) => Failure(e)
    }

}

sealed trait Try[+T] {

  def flatMap[U](f: (T) => Try[U]): Try[U]

  def map[U](f: (T) => U): Try[U] = flatMap(v => Try(f(v)))

  def foreach[U](f: T => U): Unit

  def isSuccess: Boolean

  def isFailure: Boolean

  def getOrElse[B >: T](f: => B): B = f

}

case class Success[+T](value: T) extends Try[T] {

  override def flatMap[U](f: T => Try[U]): Try[U] =
    try {
      f(value)
    } catch {
      case NonFatal(e) => Failure(e)
    }

  override def foreach[U](f: T => U): Unit = f(value)

  override def getOrElse[B >: T](f: => B): B = value

  override def isSuccess = true

  override def isFailure = false

}

case class Failure[T <: Nothing](exception: Throwable) extends Try[T] {

  override def flatMap[U](f: (T) => Try[U]): Try[U] = this

  override def foreach[U](f: T => U): Unit = {}

  override def isSuccess = false

  override def isFailure = true

}
