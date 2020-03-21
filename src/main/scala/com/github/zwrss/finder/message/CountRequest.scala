package com.github.zwrss.finder.message

import com.github.zwrss.finder.criteria.Criterion
import com.github.zwrss.finder.sort.Sort

case class CountRequest[Entity](
  query: Option[Criterion[Entity]] = None
)