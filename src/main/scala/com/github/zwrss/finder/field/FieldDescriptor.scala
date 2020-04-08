package com.github.zwrss.finder.field

trait FieldDescriptor[Entity, Value] {

  def name: String

  def get(entity: Entity): Seq[Value]

  def serialize(value: Value): String

  def deserialize(string: String): Value

}