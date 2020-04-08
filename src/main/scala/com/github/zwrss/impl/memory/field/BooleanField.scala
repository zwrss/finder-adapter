package com.github.zwrss.impl.memory.field

import com.github.zwrss.finder.field.FieldDescriptor

trait BooleanField[E] {
  this: MemoryFieldDescriptor[E, Boolean] =>

  override def tpe: String = "boolean"

  override def serialize(v: Boolean): String = v.toString

  override def deserialize(v: String): Boolean = v.toBoolean

}
