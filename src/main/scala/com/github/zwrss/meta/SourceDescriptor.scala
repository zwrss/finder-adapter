package com.github.zwrss.meta

import com.github.zwrss.finder.FieldsDescriptor
import com.github.zwrss.finder.Finder
import play.api.libs.json.Format
import play.api.libs.json.JsResult
import play.api.libs.json.JsValue


trait SourceDescriptor {
  def source: String

  def name: String

  private[meta] def _finder: Finder[_, _] with FieldsDescriptor[_] = finder

  protected def finder: Finder[_, _] with FieldsDescriptor[_]
}

object SourceDescriptor {

  implicit def format: Format[SourceDescriptor] = new Format[SourceDescriptor] {
    override def reads(json: JsValue): JsResult[SourceDescriptor] = (json \ "source").as[String] match {
      case "csv" => CsvSourceDescriptor.format.reads(json)
    }

    override def writes(o: SourceDescriptor): JsValue = o match {
      case c: CsvSourceDescriptor => CsvSourceDescriptor.format.writes(c)
    }
  }

}