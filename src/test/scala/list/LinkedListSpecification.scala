package list

import org.specs2.mutable.Specification

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

  }

}
