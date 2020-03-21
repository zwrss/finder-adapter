package com.github.zwrss.finder.message

import com.github.zwrss.finder.criteria.Criterion
import com.github.zwrss.finder.field.Field

case class TermsRequest[Entity](
  query: Option[Criterion[Entity]],
  fields: Seq[Field[Entity, _]]
)