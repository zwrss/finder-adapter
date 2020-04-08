package com.github.zwrss.impl.impl.rest.criteria

import com.github.zwrss.finder.criteria.Criterion
import com.github.zwrss.finder.criteria.CriterionDescriptor
import play.api.libs.json.JsValue

abstract class RCriterionDescriptor[Value](_name: String) extends CriterionDescriptor[JsValue, Value] {

  override def name: String = _name

  def serialize(value: Value): String

  override def ===(value: Value): Criterion[JsValue] = REquals(_name, serialize(value))

  override def like(value: Value): Criterion[JsValue] = ???

  override def in(value: Value, values: Value*): Criterion[JsValue] = ???

  override def between(min: Option[Value], max: Option[Value]): Criterion[JsValue] = ???

  override def present: Criterion[JsValue] = ???

}
