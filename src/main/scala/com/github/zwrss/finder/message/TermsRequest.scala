package com.github.zwrss.finder.message

import com.github.zwrss.finder.criteria.Criterion
import com.github.zwrss.finder.field.FieldDescriptor

case class TermsRequest[Entity](
  query: Option[Criterion[Entity]],
  fields: Seq[FieldDescriptor[Entity, _]]
)