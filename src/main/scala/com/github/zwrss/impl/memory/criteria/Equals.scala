package com.github.zwrss.impl.memory.criteria

import com.github.zwrss.finder.field.FieldDescriptor

case class Equals[Entity, Value](field: FieldDescriptor[Entity, Value], value: Value) extends MemoryCriterion[Entity] {
  override def ok(e: Entity): Boolean = (field get e) contains value
}