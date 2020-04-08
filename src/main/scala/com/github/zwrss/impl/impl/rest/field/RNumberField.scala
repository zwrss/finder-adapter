package com.github.zwrss.impl.impl.rest.field

trait RNumberField {

  def serialize(v: BigDecimal): String = v.toString

  def deserialize(v: String): BigDecimal = BigDecimal(v)

}