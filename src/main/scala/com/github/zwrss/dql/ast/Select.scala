package com.github.zwrss.dql.ast

import com.github.zwrss.finder.Source
import com.github.zwrss.finder.criteria.Criterion
import com.github.zwrss.finder.criteria.CriterionDescriptor
import com.github.zwrss.finder.field.FieldDescriptor
import com.github.zwrss.finder.message.FindRequest
import com.github.zwrss.finder.sort.Sort

case class Select(what: SelectorAST, from: String, join: Option[JoinAST], where: Option[CriterionAST] = None, sort: Option[SortAST] = None, limit: Option[LimitAST] = None) extends DqlCommand {
  override def execute[Entity](sources: Map[String, Source[_]]): DqlCommandResult = {

    val source = getSource[Entity](sources)

    val fields: Seq[FieldDescriptor[Any, Any]] = what match {
      case AllAST => source.fieldDescriptors.sortBy(_.name).map(_.asInstanceOf[FieldDescriptor[Any, Any]])
      case FieldsAST(fields) => fields.map(source._getFieldDescriptor)
    }

    def getResults(q2: Option[Criterion[_]] = None): Seq[Seq[String]] = {

      val q1 = where.map(_.toQuery(source, sources).asInstanceOf[Criterion[Entity]])

      val query: Option[Criterion[Entity]] = (q1, q2) match {
        case (Some(q1), Some(q2)) => Option(q1 && q2.asInstanceOf[Criterion[Entity]])
        case (Some(q), _) => Option(q)
        case (_, Some(q)) => Option(q.asInstanceOf[Criterion[Entity]])
        case _ => None
      }

      val sorts: Option[Sort[Entity]] = sort.map(_.toSort(source).asInstanceOf[Sort[Entity]])

      val effectiveLimit: Long = limit.map(_.limit) getOrElse 10L

      val results = source.find(FindRequest(query, sorts, limit.flatMap(_.offset) getOrElse 0L, effectiveLimit)).items.take(effectiveLimit.toInt)

      results.map { o =>
        fields.map { field =>
          field.get(o).map(field.serialize).mkString(", ")
        }.toList
      }.toList

    }

    join match {
      case Some(join) =>
        val leftCriterionDescriptor: CriterionDescriptor[Any, Any] = source._getCriterionDescriptor(join.left)
        val rightSource: Source[Any] = sources(join.source).asInstanceOf[Source[Any]]
        val rightElems: Seq[String] = rightSource.find(FindRequest[Any](limit = join.limit getOrElse 10l)).items.map { item =>
          rightSource._getFieldDescriptor(join.right).get(item).mkString(" ")
        }
        val results = rightElems.flatMap { elem =>
          getResults(Option(leftCriterionDescriptor === leftCriterionDescriptor.deserialize(elem)))
        }
        DqlCommandResult(fields.map(_.name).toList, results)
      case _ =>
        DqlCommandResult(fields.map(_.name).toList, getResults())
    }

  }
}

case class LimitAST(offset: Option[Long], limit: Long)

