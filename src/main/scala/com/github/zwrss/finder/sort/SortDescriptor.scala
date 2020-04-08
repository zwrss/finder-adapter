package com.github.zwrss.finder.sort

trait SortDescriptor[Entity] {

  def name: String

  def asc: Sort[Entity]

  def desc: Sort[Entity]

}