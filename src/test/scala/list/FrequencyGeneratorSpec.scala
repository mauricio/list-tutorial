package list

import org.specs2.mutable.Specification

class FrequencyGeneratorSpec extends Specification {

  def generate(size: Int, counts: Array[Int], values: Array[Int]): List[Int] =
    Frequency.generate(counts, values).take(size).toList

  "generator" should {

    "generate correctly for Array(1, 2), Array(4, 5)" in {
      val result = generate(6, Array(1, 2), Array(4, 5))
      result === List(4, 5, 5, 4, 5, 5)
    }

    "generate corectly for Array(3, 0, 1), Array(2, 7, 9)" in {
      val result = generate(6, Array(3, 0, 1), Array(2, 7, 9))
      result === List(2, 2, 2, 9, 2, 2)
    }

  }

}
