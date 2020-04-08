package com.github.zwrss.finder.message

import com.github.zwrss.finder.field.FieldDescriptor

case class TermsResponse[Entity](
  terms: Seq[TermsBucket[Entity, _]]
)

case class TermsBucket[Entity, Value](
  field: FieldDescriptor[Entity, Value],
  values: Seq[TermsValue[Value]]
)

case class TermsValue[Value](
  key: Value,
  stringKey: String,
  count: Long
)