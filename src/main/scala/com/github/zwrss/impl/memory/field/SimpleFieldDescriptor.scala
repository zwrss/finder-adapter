package com.github.zwrss.impl.memory.field

abstract class SimpleFieldDescriptor[E, V: Ordering](_name: String, _get: E => V) extends MemoryFieldDescriptor[E, V](_name) {

  final override def get(e: E): Seq[V] = Seq(_get(e))

}