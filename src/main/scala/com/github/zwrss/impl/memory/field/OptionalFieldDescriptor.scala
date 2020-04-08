package com.github.zwrss.impl.memory.field

abstract class OptionalFieldDescriptor[E, V: Ordering](_name: String, _get: E => Option[V]) extends MemoryFieldDescriptor[E, V](_name) {

  final override def get(e: E): Seq[V] = _get(e).toSeq

}