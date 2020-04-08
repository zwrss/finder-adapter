package com.github.zwrss.dql.ast

import com.github.zwrss.finder.criteria._


trait CriterionAST {
  def toQuery(f: CriteriaDescriptor[_]): Criterion[Any]

  def &&(that: CriterionAST): AndAST = AndAST(this, that)

  def ||(that: CriterionAST): OrAST = OrAST(this, that)

  def unary_! : NotAST = NotAST(this)
}

case class CriterionASTExt(str: String) extends AnyVal {
  def ===(value: String) = EqualsAST(str, value)

  def like(value: String) = LikeAST(str, value)

  def in(value: String, values: String*) = InAST(str, value :: values.toList)

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
  override def toQuery(f: CriteriaDescriptor[_]): Criterion[Any] = (q1 toQuery f) && (q2 toQuery f)
}

case class OrAST(q1: CriterionAST, q2: CriterionAST) extends CriterionAST {
  override def toQuery(f: CriteriaDescriptor[_]): Criterion[Any] = (q1 toQuery f) || (q2 toQuery f)
}

case class NotAST(q: CriterionAST) extends CriterionAST {
  override def toQuery(f: CriteriaDescriptor[_]): Criterion[Any] = !(q toQuery f)
}

case class ExistsAST(field: String) extends CriterionAST {
  override def toQuery(f: CriteriaDescriptor[_]): Criterion[Any] = (f _getCriterionDescriptor field).present
}

case class EqualsAST(field: String, value: String) extends CriterionAST {
  override def toQuery(f: CriteriaDescriptor[_]): Criterion[Any] = {
    val _field = f _getCriterionDescriptor field
    _field === (_field deserialize value)
  }
}

case class LikeAST(field: String, value: String) extends CriterionAST {
  override def toQuery(f: CriteriaDescriptor[_]): Criterion[Any] = {
    val _field = (f _getCriterionDescriptor field)
    _field like (_field deserialize value)
  }
}

case class InAST(field: String, values: Seq[String]) extends CriterionAST {
  override def toQuery(f: CriteriaDescriptor[_]): Criterion[Any] = {
    val _field = f _getCriterionDescriptor field
    _field in (values map _field.deserialize)
  }
}

case class RangeAST(field: String, min: Option[String] = None, max: Option[String] = None) extends CriterionAST {
  override def toQuery(f: CriteriaDescriptor[_]): Criterion[Any] = {
    val _field = f _getCriterionDescriptor field
    _field.between(min map _field.deserialize, max map _field.deserialize)
  }
}