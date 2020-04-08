package com.github.zwrss.impl.memory.criteria

import com.github.zwrss.finder.field.FieldDescriptor

case class Exists[Entity](field: FieldDescriptor[Entity, _]) extends MemoryCriterion[Entity] {
  override def ok(e: Entity): Boolean = (field get e).nonEmpty
}