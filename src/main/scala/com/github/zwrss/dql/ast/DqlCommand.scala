package com.github.zwrss.dql.ast

import com.github.zwrss.dql.parser.CountParser
import com.github.zwrss.dql.parser.SelectParser
import com.github.zwrss.dql.parser.TermsParser
import com.github.zwrss.finder.FieldsDescriptor
import com.github.zwrss.finder.Finder

trait DqlCommand {
  def execute[Entity](sources: Map[String, Finder[_, _] with FieldsDescriptor[_]]): DqlCommandResult

  protected def getSource[Entity](sources: Map[String, Finder[_, _] with FieldsDescriptor[_]]): Finder[_, Entity] with FieldsDescriptor[Entity] = {
    sources(from).asInstanceOf[Finder[_, Entity] with FieldsDescriptor[Entity]]
  }

  def from: String
}

object DqlCommand {

  private val parser = new SelectParser with TermsParser with CountParser {
    def parse(dql: String): DqlCommand = parseAll(Select | Terms | Count, dql) match {
      case Success(result, _) => result
      case NoSuccess(message, _) => sys.error(s"Cannot parse [$dql]: " + message)
    }
  }

  def fromDql(dql: String): DqlCommand = parser parse dql
}

case class DqlCommandResult(headers: Seq[String], values: Seq[Seq[String]])

object DqlCommandResult {
  def simple(values: (String, Any)*): DqlCommandResult = {
    new DqlCommandResult(values.map(_._1).toList, List(values.map(_._2.toString).toList))
  }
}