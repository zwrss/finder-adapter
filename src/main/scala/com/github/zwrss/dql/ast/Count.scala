package com.github.zwrss.dql.ast

import com.github.zwrss.finder.FieldsDescriptor
import com.github.zwrss.finder.Finder
import com.github.zwrss.finder.message.CountRequest

case class Count(from: String, where: Option[CriterionAST] = None) extends DqlCommand {
  override def execute[Entity](sources: Map[String, Finder[_, _] with FieldsDescriptor[_]]): DqlCommandResult = {
    val source = getSource(sources)
    val query = where map (_ toQuery source)
    val count = source.asInstanceOf[Finder[Any, Any]].count(CountRequest(query)).total
    DqlCommandResult.simple("count" -> count)
  }
}

