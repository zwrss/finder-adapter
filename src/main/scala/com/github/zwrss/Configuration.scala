package com.github.zwrss

import com.github.zwrss.meta.SourceDefinition
import play.api.libs.json.Format
import play.api.libs.json.Json

case class Configuration(sources: Seq[SourceDefinition])

object Configuration {
  implicit def format: Format[Configuration] = Json.using[Json.WithDefaultValues].format
}
