package com.github.zwrss.impl.memory.field

import com.github.zwrss.finder.field.FieldDescriptor

trait BigDecimalField[E] {
  this: MemoryFieldDescriptor[E, BigDecimal] =>

  override def serialize(v: BigDecimal): String = v.toString

  override def deserialize(v: String): BigDecimal = BigDecimal(v)

}