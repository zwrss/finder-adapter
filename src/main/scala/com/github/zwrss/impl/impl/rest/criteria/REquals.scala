package com.github.zwrss.impl.impl.rest.criteria

case class REquals(parameter: String, value: String) extends RCriterion {
  override def parameters: Map[String, Seq[String]] = Map(parameter -> List(value))
}
