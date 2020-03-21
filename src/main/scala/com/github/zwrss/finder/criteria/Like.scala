package com.github.zwrss.finder.criteria

import com.github.zwrss.finder.field.Field

case class Like[Entity, Value](field: Field[Entity, Value], value: Value) extends Criterion[Entity] {
  override def ok(e: Entity): Boolean = (field get e).exists {
    case x: String => x.toLowerCase matches value.toString
    case x => x == value
  }
}