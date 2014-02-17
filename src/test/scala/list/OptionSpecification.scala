package list

import org.specs2.mutable.Specification

class OptionSpecification extends Specification {

  "option" should {

    "be something" in {
      val item = Some("10")
      item.get() === "10"
    }

    "be mapped to some value in a for comprehension" in  {
      val upper = for ( name <- Some("Joe") ) yield name.toUpperCase
      upper.get() === "JOE"
    }

    "be mapped to some value manually" in {
      val number = Some("10").map(n => n.toInt)
      number.get() === 10
    }

    "it can't be anything if it is a none" in {
      val something : Option[Int] = None
      val result = something.map( x => x * 5)
      result.isDefined must beFalse
    }

    "be getOrElse the string" in {
      val item = Some("10")
      item.getOrElse("25") === "10"
    }

    "be left unit" in {
      val multiplier : Int => Option[Int] = v => Some(v * v)
      val item = Some(10).flatMap(multiplier)

      item === multiplier(10)
    }

    "be right unit" in {
      val value = Some(50).flatMap(v => Some(v))
      value === Some(50)
    }

    "be associative" in {
      val multiplier : Int => Option[Int] = v => Some(v * v)
      val divider : Int => Option[Int] = v => Some(v/2)
      val original = Some(10)

      original.flatMap(multiplier).flatMap(divider) ===
        original.flatMap(v => multiplier(v).flatMap(divider))
    }

  }

}
