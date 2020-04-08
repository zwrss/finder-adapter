package com.github.zwrss.meta

import java.io.FileReader

import com.github.zwrss.finder.Source
import com.github.zwrss.impl.memory.field.BigDecimalField
import com.github.zwrss.impl.memory.field.MemoryFieldDescriptor
import com.github.zwrss.impl.memory.field.SimpleFieldDescriptor
import com.github.zwrss.impl.memory.field.StringField
import com.github.zwrss.impl.memory.finder.MemorySource
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import play.api.libs.json.Format
import play.api.libs.json.Json

import scala.collection.convert.ImplicitConversionsToScala._

case class CsvSourceDefinition(
  name: String,
  file: String,
  format: Option[String],
  columns: Seq[ColumnDescriptor]
) extends SourceDefinition {

  val sourceName: String = "csv"

  override protected def source: Source[_] = {
    val csvFormat = format match {
      case Some("excel") => CSVFormat.EXCEL
      case _ => CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim()
    }
    val parser = csvFormat parse new FileReader(file)
    val _fields: Seq[MemoryFieldDescriptor[CSVRecord, Any]] = columns.map(_.toField)
    new MemorySource[CSVRecord] {
      override protected def fields: Seq[MemoryFieldDescriptor[CSVRecord, _]] = _fields

      override protected def objects: Seq[CSVRecord] = parser.getRecords
    }
  }
}

object CsvSourceDefinition {
  implicit def format: Format[CsvSourceDefinition] = Json.using[Json.WithDefaultValues].format
}

case class ColumnDescriptor(name: String, valueType: String, optional: Boolean = false) {
  def toField: MemoryFieldDescriptor[CSVRecord, Any] = {
    val field = valueType match {
      case "string" => new SimpleFieldDescriptor[CSVRecord, String](name, x => x get name) with StringField[CSVRecord]
      case "number" => new SimpleFieldDescriptor[CSVRecord, BigDecimal](name, x => BigDecimal(x get name)) with BigDecimalField[CSVRecord]
    }
    field.asInstanceOf[MemoryFieldDescriptor[CSVRecord, Any]]
  }
}

object ColumnDescriptor {
  implicit def format: Format[ColumnDescriptor] = Json.using[Json.WithDefaultValues].format
}