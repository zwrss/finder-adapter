package com.github.zwrss.finder.field


trait BigDecimalField[E] {
  this: Field[E, BigDecimal] =>

  override def serialize(v: BigDecimal): String = v.toString

  override def deserialize(v: String): BigDecimal = BigDecimal(v)

}