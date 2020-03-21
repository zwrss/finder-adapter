package com.github.zwrss.finder.field


trait StringField[E] {
  this: Field[E, String] =>

  def serialize(v: String): String = v

  def deserialize(v: String): String = v

}