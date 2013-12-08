package list

import scala.annotation.tailrec

sealed trait LinkedList[+E] {

  def size : Int

  def foreach(f : (E) => Unit) {
    @tailrec def loop( items : LinkedList[E] ) {
      items match {
        case Node(head,tail) => {
          f(head)
          loop(tail)
        }
        case Empty => {}
      }
    }

    loop(this)
  }

  def ::[B >: E](element : B) : LinkedList[B] = Node(element, this)

  def :::[B >: E](prefix : LinkedList[B]) : LinkedList[B] = {

    @tailrec def helper(acc : LinkedList[B], other : LinkedList[B]) : LinkedList[B] = {
      other match {
        case Node(head,tail) => helper(head :: acc, tail)
        case Empty => acc
      }
    }

    helper(this, prefix.reverse())
  }

  def map[R](f: E => R): LinkedList[R] = foldRight(LinkedList[R]()) {
    (item, acc) =>
      Node(f(item), acc)
  }

  def reverse(): LinkedList[E] = {
    foldLeft(LinkedList[E]()) {
      (acc, item) =>
        Node(item, acc)
    }
  }

  def foldRight[B](accumulator: B)(f: (E, B) => B): B = {
    reverse().foldLeft(accumulator)((acc, item) => f(item, acc))
  }

  def filter(f: (E) => Boolean): LinkedList[E] = {
    foldRight(LinkedList[E]()) {
      (item, acc) =>
        if (f(item)) {
          Node(item, acc)
        } else {
          acc
        }
    }
  }

  @tailrec final def foldLeft[B](accumulator: B)(f: (B, E) => B): B = {
    this match {
      case Node(head, tail) => {
        val current = f(accumulator, head)
        tail.foldLeft(current)(f)
      }
      case Empty => accumulator
    }
  }

}

object LinkedList {

  def apply[E](items: E*): LinkedList[E] = {
    if (items.isEmpty) {
      Empty
    } else {
      Node(items.head, apply(items.tail: _*))
    }
  }

  def sum(numbers: LinkedList[Int]): Int = {
    numbers match {
      case Node(head, tail) => head + sum(tail)
      case Empty => 0
    }
  }

  def join(numbers: LinkedList[String]): String = {
    numbers match {
      case Node(head, tail) => head + join(tail)
      case Empty => ""
    }
  }

}

case class Node[+E](head: E, tail: LinkedList[E]) extends LinkedList[E] {
  val size = 1 + tail.size
}

case object Empty extends LinkedList[Nothing] {
  val size = 0
}