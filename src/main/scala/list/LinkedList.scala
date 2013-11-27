package list

import scala.annotation.tailrec

sealed trait LinkedList[+E] {

  def map[R](f: E => R): LinkedList[R] = {
    this match {
      case Node(head, tail) => Node(f(head), tail.map(f))
      case Empty => Empty
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

case class Node[+E](head: E, tail: LinkedList[E]) extends LinkedList[E]

case object Empty extends LinkedList[Nothing]