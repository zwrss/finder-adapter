package com.github.zwrss.finder.sort

trait Sort[Entity] {

  def ordering: Ordering[Entity]

  def and(that: Sort[Entity]): Sort[Entity] = ComplexSort(this, that)

}