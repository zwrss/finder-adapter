package com.github.zwrss.finder.criteria

trait Criterion[Entity] {

  def forceOk(e: Any): Boolean = ok(e.asInstanceOf[Entity])

  def ok(e: Entity): Boolean

  def &&(that: Criterion[Entity]): And[Entity] = And(this, that)

  def ||(that: Criterion[Entity]): Or[Entity] = Or(this, that)

  def unary_! : Not[Entity] = Not(this)

}