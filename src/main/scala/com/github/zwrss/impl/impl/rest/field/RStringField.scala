package com.github.zwrss.impl.impl.rest.field

trait RStringField {

  def serialize(v: String): String = v

  def deserialize(v: String): String = v

}