package com.github.zwrss.dql.ast

import com.github.zwrss.finder.Source
import com.github.zwrss.finder.criteria.Criterion
import com.github.zwrss.finder.field.FieldDescriptor
import com.github.zwrss.finder.message.TermsBucket
import com.github.zwrss.finder.message.TermsRequest
import com.github.zwrss.finder.message.TermsValue

case class Terms(what: SelectorAST, from: String, where: Option[CriterionAST] = None) extends DqlCommand {
  override def execute[Entity](sources: Map[String, Source[_]]): DqlCommandResult = {

    val source = getSource[Entity](sources)

    val fields: Seq[FieldDescriptor[Any, Any]] = what match {
      case AllAST => source.fieldDescriptors.sortBy(_.name).map(_.asInstanceOf[FieldDescriptor[Any, Any]])
      case FieldsAST(fields) => fields.map(source._getFieldDescriptor)
    }

    val query: Option[Criterion[Entity]] = where.map(_.toQuery(source, sources).asInstanceOf[Criterion[Entity]])

    val terms = source.terms(TermsRequest[Entity](query, fields.map(_.asInstanceOf[FieldDescriptor[Entity, _]]))).terms.map {
      case TermsBucket(field, values) =>
        field -> values.map {
          case TermsValue(_, stringKey, count) => stringKey -> count
        }
    }

    val allValues: Seq[Seq[String]] = terms.flatMap {
      case (field, values) => values.sortBy(-_._2).map {
        case (value, count) => List(field.name, value, count.toString)
      }
    }

    DqlCommandResult(List("field", "value", "count"), allValues)

  }
}

