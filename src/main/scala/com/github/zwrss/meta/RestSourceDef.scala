package com.github.zwrss.meta

import com.github.zwrss.finder.Source
import com.github.zwrss.finder.criteria.CriterionDescriptor
import com.github.zwrss.finder.field.FieldDescriptor
import com.github.zwrss.finder.sort.SortDescriptor
import com.github.zwrss.http.JsonHttpClient
import com.github.zwrss.impl.impl.rest.criteria.RCriterionDescriptor
import com.github.zwrss.impl.impl.rest.field.RFieldDescriptor
import com.github.zwrss.impl.impl.rest.field.RNumberField
import com.github.zwrss.impl.impl.rest.field.RStringField
import com.github.zwrss.impl.impl.rest.finder.SimpleRestSource
import com.github.zwrss.impl.impl.rest.sort.RSortDescriptor
import play.api.libs.json.Format
import play.api.libs.json.JsValue
import play.api.libs.json.Json

//todo use parts of swagger api definition ?
case class RestSourceDef(
  name: String,
  url: String,
  fields: Seq[RFieldDef] = Seq.empty,
  sorts: Seq[RSortDef] = Seq.empty,
  queryParameters: Seq[RParamDef] = Seq.empty,
  totalPath: Option[String] = None,
  itemsPath: Option[String] = None
) extends SourceDefinition {

  val sourceName: String = "csv"

  private def _url: String = url

  override protected def source: Source[_] = {

    val _this = this

    new SimpleRestSource[JsValue] {
      override protected def http: JsonHttpClient = JsonHttpClient.default

      override protected def url: String = _this.url

      override protected def itemsPath: String = _this.itemsPath getOrElse "items"

      override protected def totalPath: String = _this.totalPath getOrElse "total"

      override def fieldDescriptors: Seq[FieldDescriptor[JsValue, _]] = fields.map(_.toField)

      override def sortDescriptors: Seq[SortDescriptor[JsValue]] = sorts.map(_.toSortDescriptor)

      override def criteriaDescriptors: Seq[CriterionDescriptor[JsValue, _]] = queryParameters.map(_.toCriterionDescriptor)
    }
  }

}

object RestSourceDef {
  implicit def format: Format[RestSourceDef] = Json.using[Json.WithDefaultValues].format
}

case class RFieldDef(name: String, valueType: String, optional: Boolean = true) {
  def toField: FieldDescriptor[JsValue, Any] = {
    val field = valueType match {
      case "string" => new RFieldDescriptor[String](name) with RStringField
      case "number" => new RFieldDescriptor[BigDecimal](name) with RNumberField
    }
    field.asInstanceOf[FieldDescriptor[JsValue, Any]]
  }
}

object RFieldDef {
  implicit def format: Format[RFieldDef] = Json.using[Json.WithDefaultValues].format
}

case class RSortDef(parameter: String, value: String) {
  def toSortDescriptor: SortDescriptor[JsValue] = {
    val descriptor = new RSortDescriptor(parameter, value)
    descriptor.asInstanceOf[SortDescriptor[JsValue]]
  }
}

object RSortDef {
  implicit def format: Format[RSortDef] = Json.using[Json.WithDefaultValues].format
}

case class RParamDef(name: String, valueType: String) {
  def toCriterionDescriptor: CriterionDescriptor[JsValue, Any] = {
    val descriptor = valueType match {
      case "string" => new RCriterionDescriptor[String](name) with RStringField
      case "number" => new RCriterionDescriptor[BigDecimal](name) with RNumberField
    }
    descriptor.asInstanceOf[CriterionDescriptor[JsValue, Any]]
  }
}

object RParamDef {
  implicit def format: Format[RParamDef] = Json.using[Json.WithDefaultValues].format
}