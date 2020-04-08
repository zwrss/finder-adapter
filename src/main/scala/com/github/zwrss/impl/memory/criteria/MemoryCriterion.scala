package com.github.zwrss.impl.memory.criteria

import com.github.zwrss.finder.criteria.Criterion

trait MemoryCriterion[Entity] extends Criterion[Entity] {

  def forceOk(e: Any): Boolean = ok(e.asInstanceOf[Entity])

  def ok(e: Entity): Boolean

  override def &&(that: Criterion[Entity]): And[Entity] = And(this, that.asInstanceOf[MemoryCriterion[Entity]])

  override def ||(that: Criterion[Entity]): Or[Entity] = Or(this, that.asInstanceOf[MemoryCriterion[Entity]])

  override def unary_! : Not[Entity] = Not(this)

}