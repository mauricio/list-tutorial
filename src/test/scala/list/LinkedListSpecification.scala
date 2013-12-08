package list

import org.specs2.mutable.Specification
import scala.collection.mutable.ListBuffer

class LinkedListSpecification extends Specification {

  "linked list" should {

    "map correctly" in {
      val original = LinkedList(2, 3, 4)
      original.map(x => x * x) === LinkedList(4, 9, 16)
    }

    "foldLeft summing all numbers" in {
      val original = LinkedList(2, 3, 4)
      original.foldLeft(0)((acc, item) => acc + item) === 9
    }

    "foldLeft making a single string from all numbers" in {
      val original = LinkedList(2, 3, 4)
      original.foldLeft(new StringBuilder())((acc, item) => acc.append(item)).toString() === "234"
    }

    "map and foldLeft to sum the squares" in {
      val original = LinkedList(2, 3, 4)
      original.map(x => x * x).foldLeft(0)((acc, x) => acc + x) === 29
    }

    "Reverse a list" in {
      val original = LinkedList(1, 2, 3, 4, 5)
      original.reverse() === LinkedList(5, 4, 3, 2, 1)
    }

    "filter a list" in {
      val original = LinkedList(1, 2, 3, 4, 5)
      original.filter(x => (x % 2) == 0) === LinkedList(2, 4)
    }

    "count items in a list" in {
      LinkedList(1, 2, 4).size === 3
      LinkedList(1, 2).size === 2
      LinkedList(1, 2, 3, 4, 5, 6).size === 6
    }

    "build lists with cons" in {
      LinkedList(1, 2, 3, 4) == 1 :: 2 :: 3 :: 4 :: Empty
    }

    "build lists with cons manually" in {
      LinkedList(1, 2, 3, 4) == Empty.::(4).::(3).::(2).::(1)
    }

    "appending two lists" in {
      val current = LinkedList(1, 2, 3, 4)
      val other = LinkedList(10, 11, 12, 13)

      ( other ::: current ) === LinkedList(10, 11, 12, 13, 1, 2, 3, 4)
    }

    "foreach implementation" in {
      val items = new ListBuffer[Int]()

      LinkedList(1, 2, 3, 4).foreach( (x) => items += x )

      items === List(1, 2, 3, 4)
    }

  }

}
