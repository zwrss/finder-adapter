package com.github.zwrss.finder.criteria

trait Criterion[Entity] {

  def &&(that: Criterion[Entity]): Criterion[Entity]

  def ||(that: Criterion[Entity]): Criterion[Entity]

  def unary_! : Criterion[Entity]

}