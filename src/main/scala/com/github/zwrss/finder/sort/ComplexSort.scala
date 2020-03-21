package com.github.zwrss.finder.sort

case class ComplexSort[Entity](s1: Sort[Entity], s2: Sort[Entity]) extends Sort[Entity] {

  override def ordering: Ordering[Entity] = (x: Entity, y: Entity) => s1.ordering.compare(x, y) match {
    case 0 => s2.ordering.compare(x, y)
    case x => x
  }

}