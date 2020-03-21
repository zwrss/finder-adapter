package com.github.zwrss.finder.field

import com.github.zwrss.finder.criteria._
import com.github.zwrss.finder.sort.SimpleSort
import com.github.zwrss.finder.sort.Sort

abstract class Field[Entity, Value: Ordering] {

  def name: String

  def get(entity: Entity): Seq[Value]

  def serialize(value: Value): String

  def deserialize(string: String): Value

  def ===(value: Value): Equals[Entity, Value] = Equals[Entity, Value](this, value)

  def like(value: Value): Like[Entity, Value] = Like[Entity, Value](this, value)

  def in(value: Value, values: Value*): In[Entity, Value] = In[Entity, Value](this, value :: values.toList)

  def <==(value: Value): Range[Entity, Value] = Range[Entity, Value](this, max = Option(value))

  def >==(value: Value): Range[Entity, Value] = Range[Entity, Value](this, min = Option(value))

  def between(min: Value): BetweenExt[Entity, Value] = BetweenExt[Entity, Value](this, min)

  def missing: Not[Entity] = !present

  def present: Exists[Entity] = Exists[Entity](this)

  def asc: Sort[Entity] = SimpleSort[Entity, Value](this, true)

  def desc: Sort[Entity] = SimpleSort[Entity, Value](this, false)

  def _getOrdering: Ordering[Value] = implicitly

}

case class BetweenExt[E, V](field: Field[E, V], min: V) {
  def and(max: V): Range[E, V] = Range(field, Option(min), Option(max))
}
