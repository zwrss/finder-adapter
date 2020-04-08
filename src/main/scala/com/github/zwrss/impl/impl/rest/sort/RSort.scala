package com.github.zwrss.impl.impl.rest.sort

import com.github.zwrss.finder.sort.Sort
import com.github.zwrss.util.MapMerge
import play.api.libs.json.JsValue

trait RSort extends Sort[JsValue] {
  override def and(that: Sort[JsValue]): Sort[JsValue] = RComplexSort(this, that.asInstanceOf[RSort])

  def parameters: Map[String, Seq[String]]
}

case class RComplexSort(s1: RSort, s2: RSort) extends RSort {
  override def parameters: Map[String, Seq[String]] = MapMerge.merge(s1.parameters, s2.parameters)
}

case class RSimpleSort(parameter: String, value: String, ascending: Boolean) extends RSort {
  override def parameters: Map[String, Seq[String]] = {
    val _value = if (ascending) value else s"-$value"
    Map(parameter -> Seq(_value))
  }
}
