package com.github.zwrss.meta

import java.io.FileReader

import com.github.zwrss.finder.FieldsDescriptor
import com.github.zwrss.finder.Finder
import com.github.zwrss.finder.field.BigDecimalField
import com.github.zwrss.finder.field.Field
import com.github.zwrss.finder.field.SimpleField
import com.github.zwrss.finder.field.StringField
import com.github.zwrss.memoryfinder.MemoryFinder
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import play.api.libs.json.Format
import play.api.libs.json.Json

import scala.collection.convert.ImplicitConversionsToScala._

case class CsvSourceDescriptor(
  name: String,
  file: String,
  format: Option[String],
  columns: Seq[ColumnDescriptor]
) extends SourceDescriptor {

  val source: String = "csv"

  override protected def finder: Finder[_, _] with FieldsDescriptor[_] = {
    val csvFormat = format match {
      case Some("excel") => CSVFormat.EXCEL
      case _ => CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim()
    }
    val parser = csvFormat parse new FileReader(file)
    val idGetter: CSVRecord => Any = x => columns.find(_.id).get.toField.get(x).head
    new MemoryFinder[Any, CSVRecord](parser.getRecords, idGetter) with FieldsDescriptor[CSVRecord] {
      override def fields: Seq[Field[CSVRecord, _]] = columns.map(_.toField)
    }
  }
}

object CsvSourceDescriptor {
  implicit def format: Format[CsvSourceDescriptor] = Json.using[Json.WithDefaultValues].format
}

case class ColumnDescriptor(name: String, valueType: String, optional: Boolean = false, id: Boolean = false) {
  def toField: Field[CSVRecord, Any] = {
    val field = valueType match {
      case "string" => new SimpleField[CSVRecord, String](name, x => x get name) with StringField[CSVRecord]
      case "number" => new SimpleField[CSVRecord, BigDecimal](name, x => BigDecimal(x get name)) with BigDecimalField[CSVRecord]
    }
    field.asInstanceOf[Field[CSVRecord, Any]]
  }
}

object ColumnDescriptor {
  implicit def format: Format[ColumnDescriptor] = Json.using[Json.WithDefaultValues].format
}