package com.github.zwrss.impl.impl.rest.sort

import com.github.zwrss.finder.sort.Sort
import com.github.zwrss.finder.sort.SortDescriptor
import play.api.libs.json.JsValue

class RSortDescriptor(parameter: String, value: String) extends SortDescriptor[JsValue] {

  override def name: String = value

  override def asc: Sort[JsValue] = RSimpleSort(parameter, value, ascending = true)

  override def desc: Sort[JsValue] = RSimpleSort(parameter, value, ascending = false)

}
