package com.github.zwrss.impl.memory.criteria

case class Not[E](q: MemoryCriterion[E]) extends MemoryCriterion[E] {
  override def ok(e: E): Boolean = !(q ok e)
}