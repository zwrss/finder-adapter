package com.github.zwrss.dql.ast

import com.github.zwrss.finder.Source
import com.github.zwrss.finder.message.CountRequest

case class Count(from: String, where: Option[CriterionAST] = None) extends DqlCommand {
  override def execute[Entity](sources: Map[String, Source[_]]): DqlCommandResult = {
    val source = getSource[Any](sources)
    val query = where map (_ toQuery source)
    val count = source.count(CountRequest(query)).total
    DqlCommandResult.simple("count" -> count)
  }
}

