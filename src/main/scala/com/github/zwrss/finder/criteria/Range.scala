package com.github.zwrss.finder.criteria

import com.github.zwrss.finder.field.Field
import org.joda.time.DateTime

case class Range[Entity, Value](field: Field[Entity, Value], min: Option[Value] = None, max: Option[Value] = None) extends Criterion[Entity] {

  private def compare(_x: Any, cast: Any => BigDecimal): Boolean = {
    val x = cast(_x)
    min.forall(min => x >= cast(min)) && max.forall(max => x <= cast(max))
  }

  override def ok(e: Entity): Boolean = field.get(e).exists {
    case x: Int => compare(x, BigDecimal apply _.asInstanceOf[Int])
    case x: Long => compare(x, BigDecimal apply _.asInstanceOf[Long])
    case x: Float => compare(x, BigDecimal apply _.asInstanceOf[Float].toDouble)
    case x: Double => compare(x, BigDecimal apply _.asInstanceOf[Double])
    case x: BigDecimal => compare(x, _.asInstanceOf[BigDecimal])
    case x: String =>
      min.forall(_.toString <= x) && max.forall(_.toString >= x)
    case x: DateTime =>
      min.forall(_.asInstanceOf[DateTime] isBefore x) && max.forall(_.asInstanceOf[DateTime] isAfter x)
    case _ => false
  }
}