package com.github.zwrss.finder.criteria

import com.github.zwrss.finder.field.Field

case class Exists[Entity](field: Field[Entity, _]) extends Criterion[Entity] {
  override def ok(e: Entity): Boolean = (field get e).nonEmpty
}