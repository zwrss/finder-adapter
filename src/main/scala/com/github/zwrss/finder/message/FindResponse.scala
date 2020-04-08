package com.github.zwrss.finder.message

import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes

case class FindResponse[Entity](
  total: Long,
  items: Seq[Entity]
)

object FindResponse {

  implicit def reads[Entity: Reads]: Reads[FindResponse[Entity]] = Json.using[Json.WithDefaultValues].reads[FindResponse[Entity]]

  implicit def writes[Entity: Writes]: Writes[FindResponse[Entity]] = Json.using[Json.WithDefaultValues].writes[FindResponse[Entity]]

}