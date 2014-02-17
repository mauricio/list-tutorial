package list

object Frequency {

  def generate[T](counts : Array[Int], values : Array[T]) : Stream[T] =
    counts.zip(values).toStream.flatMap(pair => Stream.fill(pair._1)(pair._2))

}
