package com.github.zwrss.finder.sort

trait Sort[Entity] {

  def and(that: Sort[Entity]): Sort[Entity]

}