package com.github.zwrss.dql.ast

import com.github.zwrss.finder.FieldsDescriptor
import com.github.zwrss.finder.sort.ComplexSort
import com.github.zwrss.finder.sort.SimpleSort
import com.github.zwrss.finder.sort.Sort

trait SortAST {
  def toSort(f: FieldsDescriptor[_]): Sort[Any]

  def and(that: SortAST): SortAST = ComplexSortAST(this, that)
}

case class SimpleSortAST(field: String, ascending: Boolean = true) extends SortAST {
  override def toSort(f: FieldsDescriptor[_]): Sort[Any] = {
    val _field = f _getField field
    SimpleSort(_field, ascending)(_field._getOrdering)
  }
}

case class ComplexSortAST(s1: SortAST, s2: SortAST) extends SortAST {
  override def toSort(f: FieldsDescriptor[_]): Sort[Any] = ComplexSort(s1 toSort f, s2 toSort f)
}