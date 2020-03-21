package com.github.zwrss.finder.criteria

case class And[E](q1: Criterion[E], q2: Criterion[E]) extends Criterion[E] {
  override def ok(e: E): Boolean = (q1 ok e) && (q2 ok e)
}