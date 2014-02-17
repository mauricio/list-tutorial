package list

class FrequencyGenerator( counts : Array[Int], values : Array[Int] ) {
  raiseIfLessThanZero("counts", counts)
  raiseIfLessThanZero("values", values)

  private val zipped = counts.zip(values).filter(entry => entry._1 > 0)

  if ( zipped.size == 0 ) {
    throw new IllegalArgumentException("Result zip can't be empty")
  }

  private var currentIndex = 0
  private var currentCount = 0

  def next() : Int = {
    flipIfNeeded()
    currentCount += 1
    zipped(currentIndex)._2
  }

  private def flipIfNeeded() {
    if ( currentCount == zipped(currentIndex)._1 ) {
      currentCount = 0
      currentIndex = if ( currentIndex == (zipped.length - 1) ) {
        0
      } else {
        currentIndex + 1
      }
    }
  }

  private def raiseIfLessThanZero( name : String, items : Array[Int]) {
    if ( items.length == 0 ) {
      throw new IllegalArgumentException(s"${name} needs to have at least one item")
    }
  }

  def toStream() : Stream[Int] = Stream.continually( this.next() )

}
