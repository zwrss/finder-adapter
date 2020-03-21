package com.github.zwrss.finder.field


abstract class OptionalField[E, V: Ordering](_name: String, _get: E => Option[V]) extends Field[E, V] {

  final override def name: String = _name

  final override def get(e: E): Seq[V] = _get(e).toSeq

}