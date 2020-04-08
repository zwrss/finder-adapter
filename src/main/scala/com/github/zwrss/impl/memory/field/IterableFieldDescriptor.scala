package com.github.zwrss.impl.memory.field

abstract class IterableFieldDescriptor[E, V: Ordering](_name: String, _get: E => Iterable[V]) extends MemoryFieldDescriptor[E, V](_name) {

  final override def get(e: E): Seq[V] = _get(e).toSeq

}