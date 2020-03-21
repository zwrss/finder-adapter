package com.github.zwrss.dql.ast

import com.github.zwrss.finder.FieldsDescriptor
import com.github.zwrss.finder.Finder
import com.github.zwrss.finder.criteria.Criterion
import com.github.zwrss.finder.field.Field
import com.github.zwrss.finder.message.FindRequest
import com.github.zwrss.finder.sort.Sort

case class Select(what: SelectorAST, from: String, where: Option[CriterionAST] = None, sort: Option[SortAST] = None, limit: Option[LimitAST] = None) extends DqlCommand {
  override def execute[Entity](sources: Map[String, Finder[_, _] with FieldsDescriptor[_]]): DqlCommandResult = {

    val source = getSource[Entity](sources)

    val fields: Seq[Field[Any, Any]] = what match {
      case AllAST => source.fields.sortBy(_.name).map(_.asInstanceOf[Field[Any, Any]])
      case FieldsAST(fields) => fields.map(source._getField)
    }

    val query: Option[Criterion[Entity]] = where.map(_.toQuery(source).asInstanceOf[Criterion[Entity]])

    val sorts: Option[Sort[Entity]] = sort.map(_.toSort(source).asInstanceOf[Sort[Entity]])

    val results = source.find(FindRequest(query, sorts, limit.flatMap(_.offset) getOrElse 0, limit.map(_.limit) getOrElse 10)).items

    DqlCommandResult(fields.map(_.name).toList, results.map { o =>
      fields.map { field =>
        field.get(o).map(field.serialize).mkString(", ")
      }.toList
    }.toList)

  }
}

case class LimitAST(offset: Option[Long], limit: Long)

