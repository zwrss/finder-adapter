package com.github.zwrss.dql.ast

import com.github.zwrss.finder.Source
import com.github.zwrss.finder.criteria._


trait CriterionAST {
  def toQuery(f: CriteriaDescriptor[_], sources: Map[String, Source[_]]): Criterion[Any]

  def &&(that: CriterionAST): AndAST = AndAST(this, that)

  def ||(that: CriterionAST): OrAST = OrAST(this, that)

  def unary_! : NotAST = NotAST(this)
}

case class CriterionASTExt(str: String) extends AnyVal {
  def ===(value: String) = EqualsAST(str, value)

  def like(value: String) = LikeAST(str, value)

  def in(value: String, values: String*) = InAST(str, Left(value :: values.toList))

  def in(select: Select) = InAST(str, Right(select))

  def <==(value: String) = RangeAST(str, max = Option(value))

  def >==(value: String) = RangeAST(str, min = Option(value))

  def between(min: String) = BetweenCriterionASTExt(str, min)

  def missing = !present

  def present = ExistsAST(str)
}

object CriterionASTExt {
  implicit def toCriterionExt(str: String): CriterionASTExt = new CriterionASTExt(str)
}

case class BetweenCriterionASTExt(str: String, min: String) {
  def and(max: String): RangeAST = RangeAST(str, Option(min), Option(max))
}

case class AndAST(q1: CriterionAST, q2: CriterionAST) extends CriterionAST {
  override def toQuery(f: CriteriaDescriptor[_], sources: Map[String, Source[_]]): Criterion[Any] = (q1.toQuery(f, sources)) && (q2.toQuery(f, sources))
}

case class OrAST(q1: CriterionAST, q2: CriterionAST) extends CriterionAST {
  override def toQuery(f: CriteriaDescriptor[_], sources: Map[String, Source[_]]): Criterion[Any] = (q1.toQuery(f, sources)) || (q2.toQuery(f, sources))
}

case class NotAST(q: CriterionAST) extends CriterionAST {
  override def toQuery(f: CriteriaDescriptor[_], sources: Map[String, Source[_]]): Criterion[Any] = !(q.toQuery(f, sources))
}

case class ExistsAST(field: String) extends CriterionAST {
  override def toQuery(f: CriteriaDescriptor[_], sources: Map[String, Source[_]]): Criterion[Any] = (f _getCriterionDescriptor field).present
}

case class EqualsAST(field: String, value: String) extends CriterionAST {
  override def toQuery(f: CriteriaDescriptor[_], sources: Map[String, Source[_]]): Criterion[Any] = {
    val _field = f _getCriterionDescriptor field
    _field === (_field deserialize value)
  }
}

case class LikeAST(field: String, value: String) extends CriterionAST {
  override def toQuery(f: CriteriaDescriptor[_], sources: Map[String, Source[_]]): Criterion[Any] = {
    val _field = (f _getCriterionDescriptor field)
    _field like (_field deserialize value)
  }
}

case class InAST(field: String, query: Either[Seq[String], Select]) extends CriterionAST {
  override def toQuery(f: CriteriaDescriptor[_], sources: Map[String, Source[_]]): Criterion[Any] = {
    val _field = f _getCriterionDescriptor field
    val values = query match {
      case Left(values) =>
        values
      case Right(select) =>
        select.execute[Any](sources) match {
          case result if result.headers.size == 1 =>
            result.values.map(_.head)
          case _ =>
            Seq.empty[String]
        }
    }
    _field in (values map _field.deserialize)
  }
}

case class RangeAST(field: String, min: Option[String] = None, max: Option[String] = None) extends CriterionAST {
  override def toQuery(f: CriteriaDescriptor[_], sources: Map[String, Source[_]]): Criterion[Any] = {
    val _field = f _getCriterionDescriptor field
    _field.between(min map _field.deserialize, max map _field.deserialize)
  }
}