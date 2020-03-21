package com.github.zwrss.finder.field

abstract class IterableField[E, V: Ordering](_name: String, _get: E => Iterable[V]) extends Field[E, V] {

  final override def name: String = _name

  final override def get(e: E): Seq[V] = _get(e).toSeq

}