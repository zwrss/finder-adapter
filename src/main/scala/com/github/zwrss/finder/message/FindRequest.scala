package com.github.zwrss.finder.message

import com.github.zwrss.finder.criteria.Criterion
import com.github.zwrss.finder.sort.Sort

case class FindRequest[Entity](
  query: Option[Criterion[Entity]] = None,
  sort: Option[Sort[Entity]] = None,
  offset: Long = 0,
  limit: Long = 10
)