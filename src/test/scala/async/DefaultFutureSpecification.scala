package async

import org.specs2.mutable.Specification
import scala.collection.mutable.ArrayBuffer
import scala.util.{Failure, Success}

class DefaultFutureSpecification extends Specification {

  implicit val executionContext = CurrentThreadExecutionContext

  "default future" should {

    "correctly execute the callbacks once completed" in {
      val future = new DefaultFuture[String]()
      val items = new ArrayBuffer[String]()

      future.onSuccess { case value => items += value }
      future.complete(Success("some-value"))

      items === List("some-value")
      future.isCompleted must beTrue
    }

    "correctly execute the callback right away after completion" in {
      val future = new DefaultFuture[String]()
      val items = new ArrayBuffer[String]()

      future.complete(Success("some-value"))
      future.onSuccess { case value => items += value }

      items === List("some-value")
    }

    "correctly execute the many callbacks registered" in {
      val future = new DefaultFuture[String]()
      val items = new ArrayBuffer[String]()

      future.onSuccess { case value => items += value }
      future.onSuccess { case value => items += value }
      future.onSuccess { case value => items += value }
      future.complete(Success("some-value"))

      items === List("some-value", "some-value", "some-value")
    }

    "map the value into something else" in {
      val future = new DefaultFuture[String]()

      val mapped = future.map(s => s.toInt)

      future.complete(Success("1"))

      mapped.value.get.get === 1
    }

    "flatMap the future into another future" in {
      val future = new DefaultFuture[String]()
      val otherFuture = new DefaultFuture[String]()

      val result = for ( first <- future;
                         second <- otherFuture )
                      yield first.toInt + second.toInt

      future.complete(Success("5"))
      otherFuture.complete(Success("3"))

      result.value.get.get === 8
    }

    "should fail callbacks correctly" in {
      val future = new DefaultFuture[String]()
      val exception = new Exception()
      var caughtException : Throwable = null

      future.onFailure { case f =>
        caughtException = f
      }

      future.complete(Failure(exception))

      caughtException === exception
    }

    "should fail right away if failed already" in {
      val future = new DefaultFuture[String]()
      val exception = new Exception()
      var caughtException : Throwable = null

      future.complete(Failure(exception))

      future.onFailure { case f =>
        caughtException = f
      }

      caughtException === exception
    }

    "should return none when not completed" in {
      val future = new DefaultFuture[String]()
      future.value === None
    }

  }

}
