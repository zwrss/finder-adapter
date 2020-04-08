package com.github.zwrss.impl.memory.criteria

import com.github.zwrss.finder.field.FieldDescriptor

case class Like[Entity, Value](field: FieldDescriptor[Entity, Value], value: Value) extends MemoryCriterion[Entity] {
  override def ok(e: Entity): Boolean = (field get e).exists {
    case x: String => x.toLowerCase matches value.toString
    case x => x == value
  }
}