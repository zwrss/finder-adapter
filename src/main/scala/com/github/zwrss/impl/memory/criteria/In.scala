package com.github.zwrss.impl.memory.criteria

import com.github.zwrss.finder.field.FieldDescriptor

case class In[Entity, Value](field: FieldDescriptor[Entity, Value], values: Seq[Value]) extends MemoryCriterion[Entity] {
  override def ok(e: Entity): Boolean = (field get e) exists values.contains
}