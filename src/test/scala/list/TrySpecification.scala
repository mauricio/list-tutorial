package list

import org.specs2.mutable.Specification

class TrySpecification extends Specification {

  "try" should {

    "be a failure when it can't parse the number" in {
      val result = Try("abc".toInt)
      result.isFailure must beTrue
    }

    "be a success when it parses the number" in {
      val result = Try("10".toInt)
      result.isSuccess must beTrue
      result match {
        case Success(v) => v === 10
        case Failure(e) => throw new IllegalStateException("should not have come here")
      }
    }

"be composable with map" in {
  val result = Try("4".toInt).map( v => v * v ).map( v => v / 2 )

  result match {
    case Success(v) => v === 8
    case Failure(e) => throw new IllegalStateException("should not have come here")
  }
}

"be composable with flatMap/for comprehensions" in {

  val result = for (
    v <- Try("5".toInt);
    k <- Try("6".toInt);
    z <- Try("9".toInt)
  ) yield( v + k + z)

  result match {
    case Success(r) => r === 20
    case Failure(e) => throw new IllegalStateException("should not have come here")
  }
}

"fail mapping when one of the tries is a failure" in {
  val result = Try("abc".toInt).map( v => v * v)
  result.isFailure must beTrue
}

"fail flatMap when one of the tries is a failure" in {
  val result = for (
    v <- Try("5".toInt);
    k <- Try("JOE".toInt);
    z <- Try("9".toInt)
  ) yield( v + k + z)

  result.isFailure must beTrue
}

  }

}
