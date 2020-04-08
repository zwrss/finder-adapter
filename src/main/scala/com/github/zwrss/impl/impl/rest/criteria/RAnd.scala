package com.github.zwrss.impl.impl.rest.criteria

import com.github.zwrss.util.MapMerge

case class RAnd(q1: RCriterion, q2: RCriterion) extends RCriterion {
  override def parameters: Map[String, Seq[String]] = MapMerge.merge(q1.parameters, q2.parameters)
}
