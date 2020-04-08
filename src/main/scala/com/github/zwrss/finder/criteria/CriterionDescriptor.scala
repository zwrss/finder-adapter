package com.github.zwrss.finder.criteria

trait CriterionDescriptor[Entity, Value] {

  def name: String

  def tpe: String

  def deserialize(string: String): Value

  def ===(value: Value): Criterion[Entity]

  def like(value: Value): Criterion[Entity]

  def in(value: Value, values: Value*): Criterion[Entity]

  def in(values: Seq[Value]): Criterion[Entity] = {
    if (values.size > 1) in(values.head, values.tail: _*)
    else if (values.size == 1) ===(values.head)
    else missing
  }

  def <==(value: Value): Criterion[Entity] = between(None, Option(value))

  def >==(value: Value): Criterion[Entity] = between(Option(value), None)

  def between(min: Option[Value], max: Option[Value]): Criterion[Entity]

  def missing: Criterion[Entity] = !present

  def present: Criterion[Entity]

}