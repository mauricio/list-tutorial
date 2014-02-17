package list

trait Monad[+T] {

  def flatMap[U]( f : (T) => Monad[U] ) : Monad[U]
  def map[U](f : (T) => U) : Monad[U] = flatMap(v => unit(f(v)))
  def unit[B](value : B) : Monad[B]

}