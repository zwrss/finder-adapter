package com.github.zwrss.impl.memory.criteria

case class And[E](q1: MemoryCriterion[E], q2: MemoryCriterion[E]) extends MemoryCriterion[E] {
  override def ok(e: E): Boolean = (q1 ok e) && (q2 ok e)
}