package com.github.zwrss.impl.memory.sort

case class ComplexSort[Entity](s1: MemorySort[Entity], s2: MemorySort[Entity]) extends MemorySort[Entity] {

  override def ordering: Ordering[Entity] = (x: Entity, y: Entity) => s1.ordering.compare(x, y) match {
    case 0 => s2.ordering.compare(x, y)
    case x => x
  }

}