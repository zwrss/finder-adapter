package com.github.zwrss.finder.sort

import com.github.zwrss.finder.field.Field

case class SimpleSort[Entity, Value: Ordering](field: Field[Entity, Value], ascending: Boolean) extends Sort[Entity] {

  override lazy val ordering: Ordering[Entity] = {
    val valueOrderingUnoriented = implicitly[Ordering[Value]]
    val valueOrdering = if (ascending) valueOrderingUnoriented else valueOrderingUnoriented.reverse

    def getBestValue(e: Entity): Option[Value] = (field get e).sorted(valueOrdering).headOption

    (x: Entity, y: Entity) =>
      (getBestValue(x), getBestValue(y)) match {
        case (Some(a), Some(b)) => valueOrdering.compare(a, b)
        case (None, Some(b)) => 1
        case (Some(a), None) => -1
        case _ => 0
      }
  }

}