package com.github.zwrss.dql.ast

import com.github.zwrss.finder.sort.Sort
import com.github.zwrss.finder.sort.SortsDescriptor

trait SortAST {
  def toSort(f: SortsDescriptor[_]): Sort[Any]

  def and(that: SortAST): SortAST = ComplexSortAST(this, that)
}

case class SimpleSortAST(field: String, ascending: Boolean = true) extends SortAST {
  override def toSort(f: SortsDescriptor[_]): Sort[Any] = {
    val _field = f _getSortDescriptor field
    _field.asc
  }
}

case class ComplexSortAST(s1: SortAST, s2: SortAST) extends SortAST {
  override def toSort(f: SortsDescriptor[_]): Sort[Any] = {
    (s1 toSort f) and (s2 toSort f)
  }
}