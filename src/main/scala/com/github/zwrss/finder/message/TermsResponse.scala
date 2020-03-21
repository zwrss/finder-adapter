package com.github.zwrss.finder.message

import com.github.zwrss.finder.field.Field

case class TermsResponse[Entity](
  terms: Seq[TermsBucket[Entity, _]]
)

case class TermsBucket[Entity, Value](
  field: Field[Entity, Value],
  values: Seq[TermsValue[Value]]
)

case class TermsValue[Value](
  key: Value,
  stringKey: String,
  count: Long
)