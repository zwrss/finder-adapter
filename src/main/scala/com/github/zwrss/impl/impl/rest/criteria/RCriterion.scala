package com.github.zwrss.impl.impl.rest.criteria

import com.github.zwrss.finder.criteria.Criterion
import play.api.libs.json.JsValue

abstract class RCriterion extends Criterion[JsValue] {

  override def &&(that: Criterion[JsValue]): Criterion[JsValue] = RAnd(this, that.asInstanceOf[RCriterion])
  override def ||(that: Criterion[JsValue]): Criterion[JsValue] = sys.error("REST API does not support OR")
  override def unary_! : Criterion[JsValue] = sys.error("REST API does not support NOT")

  def parameters: Map[String, Seq[String]]

}