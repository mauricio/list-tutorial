package list

sealed trait Either[+L,+R] {

  def isLeft : Boolean
  def isRight : Boolean

  def fold[X](leftFunction : (L) => X, rightFunction : (R) => X) : X =
    this match {
      case Right(value) => rightFunction(value)
      case Left(value) => leftFunction(value)
    }

}

case class Right[R](value : R) extends Either[Nothing,R] {
  override def isLeft = false
  override def isRight = true
}

case class Left[L](value : L) extends Either[L,Nothing] {
  override def isLeft = true
  override def isRight = false
}
