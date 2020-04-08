package com.github.zwrss.impl.impl.rest.field

import com.github.zwrss.finder.field.FieldDescriptor
import play.api.libs.json.JsValue
import play.api.libs.json.Reads

abstract class RFieldDescriptor[Value: Reads](_name: String) extends FieldDescriptor[JsValue, Value] {

  override def name: String = _name

  override def get(entity: JsValue): Seq[Value] = {
    val lookup = entity \ name

    def simple: Option[Value] = lookup.asOpt[Value]
    def list: Option[Seq[Value]] = lookup.asOpt[Seq[Value]]

    simple.map(Seq.apply(_)) orElse list getOrElse Seq.empty
  }

}