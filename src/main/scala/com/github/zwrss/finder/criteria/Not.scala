package com.github.zwrss.finder.criteria

case class Not[E](q: Criterion[E]) extends Criterion[E] {
  override def ok(e: E): Boolean = !(q ok e)
}