package com.github.zwrss.finder.message

case class FindResponse[Entity](
  total: Long,
  items: Seq[Entity]
)
