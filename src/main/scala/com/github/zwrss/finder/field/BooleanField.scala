package com.github.zwrss.finder.field


trait BooleanField[E] {
  this: Field[E, Boolean] =>

  override def serialize(v: Boolean): String = v.toString

  override def deserialize(v: String): Boolean = v.toBoolean

}
