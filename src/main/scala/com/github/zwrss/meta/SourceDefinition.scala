package com.github.zwrss.meta

import com.github.zwrss.finder.Source
import play.api.libs.json.Format
import play.api.libs.json.JsResult
import play.api.libs.json.JsValue


trait SourceDefinition {

  def sourceName: String

  def name: String

  private[meta] def _source: Source[_] = source

  protected def source: Source[_]

}

object SourceDefinition {

  implicit def format: Format[SourceDefinition] = new Format[SourceDefinition] {
    override def reads(json: JsValue): JsResult[SourceDefinition] = (json \ "source").as[String] match {
      case "csv" => CsvSourceDefinition.format.reads(json)
      case "rest" => RestSourceDef.format.reads(json)
    }

    override def writes(o: SourceDefinition): JsValue = o match {
      case c: CsvSourceDefinition => CsvSourceDefinition.format.writes(c)
      case c: RestSourceDef => RestSourceDef.format.writes(c)
    }
  }

}