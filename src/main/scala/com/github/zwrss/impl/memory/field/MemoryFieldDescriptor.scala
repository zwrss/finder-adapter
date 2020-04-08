package com.github.zwrss.impl.memory.field

import com.github.zwrss.finder.criteria.Criterion
import com.github.zwrss.finder.criteria.CriterionDescriptor
import com.github.zwrss.finder.field.FieldDescriptor
import com.github.zwrss.impl.memory.criteria.Between
import com.github.zwrss.impl.memory.criteria.Equals
import com.github.zwrss.impl.memory.criteria.Exists
import com.github.zwrss.impl.memory.criteria.In
import com.github.zwrss.impl.memory.criteria.Like
import com.github.zwrss.impl.memory.sort.SimpleSort
import com.github.zwrss.finder.sort.Sort
import com.github.zwrss.finder.sort.SortDescriptor

abstract class MemoryFieldDescriptor[Entity, Value: Ordering](_name: String) extends FieldDescriptor[Entity, Value] with CriterionDescriptor[Entity, Value] with SortDescriptor[Entity] {

  final override def name: String = _name

  override def ===(value: Value): Criterion[Entity] = Equals(this, value)

  override def like(value: Value): Criterion[Entity] = Like(this, value)

  override def in(value: Value, values: Value*): Criterion[Entity] = In(this, value +: values)

  override def between(min: Option[Value], max: Option[Value]): Criterion[Entity] = Between(this, min, max)

  def between(min: Value): BetweenExt[Entity, Value] = BetweenExt[Entity, Value](this, min)

  override def present: Criterion[Entity] = Exists(this)

  override def asc: Sort[Entity] = SimpleSort(this, ascending = true)

  override def desc: Sort[Entity] = SimpleSort(this, ascending = false)

}

case class BetweenExt[Entity, Value](field: MemoryFieldDescriptor[Entity, Value], min: Value) {
  def and(max: Value): Between[Entity, Value] = Between(field, Option(min), Option(max))
}
