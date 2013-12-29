package list

sealed trait Option[+E] {

  def isDefined : Boolean
  def isEmpty : Boolean = !isDefined
  def size : Int = if (isDefined) 1 else 0

  def map[R](f : E => R) : Option[R]
  def flatMap[R]( f : E => Option[R] ) : Option[R] = if ( isDefined ) f(this.get) else None
  def foreach[U]( f : E => U )
  def get() : E
  def getOrElse[B >: E]( f : => B ) : B = if ( isDefined ) get() else f

}

case class Some[+E]( element : E ) extends Option[E] {

  override val isDefined = true
  override def map[R](f : E => R) : Option[R] = Some(f(element))
  override def foreach[U]( f : E => U ) = f(element)
  override def get() : E = element

}

case object None extends Option[Nothing] {

  override val isDefined = false
  override def map[R](f : Nothing => R) : Option[R] = None
  override def foreach[U]( f : Nothing => U ) = {}
  override def get() : Nothing = throw new NoSuchElementException("There is no object here, this is a None")

}