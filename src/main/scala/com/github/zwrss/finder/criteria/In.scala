package com.github.zwrss.finder.criteria

import com.github.zwrss.finder.field.Field

case class In[Entity, Value](field: Field[Entity, Value], values: Seq[Value]) extends Criterion[Entity] {
  override def ok(e: Entity): Boolean = (field get e) exists values.contains
}