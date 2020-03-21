package com.github.zwrss.finder.field

abstract class SimpleField[E, V: Ordering](_name: String, _get: E => V) extends Field[E, V] {

  final override def name: String = _name

  final override def get(e: E): Seq[V] = Seq(_get(e))

}