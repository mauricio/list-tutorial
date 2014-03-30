package async

import org.specs2.mutable.Specification
import scala.util.{Failure, Try}

class PromiseSpecification extends Specification {

  "promise" should {

    "complete with a value" in {
      val promise = Promise[String]()
      promise.complete(Try("some-value"))
      promise.isCompleted must beTrue
      promise.value.get === "some-value"
    }

    "complete with an error" in {
      val promise = Promise[String]()
      promise.complete(Failure(new Exception()))
      promise.isCompleted must beTrue
      promise.value.isFailure must beTrue
    }

    "tryComplete called many times does not complete twice" in {
      val promise = Promise[String]()
      promise.tryComplete(Try("some-value")) must beTrue
      promise.tryComplete(Try("some-other-value")) must beFalse
      promise.isCompleted must beTrue
      promise.value.get === "some-value"
    }

    "raise an error if completed more than once" in {
      val promise = Promise[String]()
      promise.complete(Try("some-value"))
      promise.complete(Try("some-other-value")) must throwA[IllegalStateException]
    }

    "should not accept null as a value" in {
      val promise = Promise[String]()
      promise.complete(null) must throwA[IllegalArgumentException]
    }

  }

}
