package com.github.zwrss.impl.memory.field

trait StringField[E] {
  this: MemoryFieldDescriptor[E, String] =>

  override def tpe: String = "string"

  def serialize(v: String): String = v

  def deserialize(v: String): String = v

}