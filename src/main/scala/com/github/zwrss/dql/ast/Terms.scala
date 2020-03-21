package com.github.zwrss.dql.ast

import com.github.zwrss.finder.FieldsDescriptor
import com.github.zwrss.finder.Finder
import com.github.zwrss.finder.criteria.Criterion
import com.github.zwrss.finder.field.Field
import com.github.zwrss.finder.message.TermsBucket
import com.github.zwrss.finder.message.TermsRequest
import com.github.zwrss.finder.message.TermsValue

case class Terms(what: SelectorAST, from: String, where: Option[CriterionAST] = None) extends DqlCommand {
  override def execute[Entity](sources: Map[String, Finder[_, _] with FieldsDescriptor[_]]): DqlCommandResult = {

    val source = getSource[Entity](sources)

    val fields: Seq[Field[Any, Any]] = what match {
      case AllAST => source.fields.sortBy(_.name).map(_.asInstanceOf[Field[Any, Any]])
      case FieldsAST(fields) => fields.map(source._getField)
    }

    val query: Option[Criterion[Entity]] = where.map(_.toQuery(source).asInstanceOf[Criterion[Entity]])

    val terms = source.terms(TermsRequest[Entity](query, fields.map(_.asInstanceOf[Field[Entity, _]]))).terms.map {
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

