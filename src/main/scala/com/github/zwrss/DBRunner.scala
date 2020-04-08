package com.github.zwrss

import com.github.zwrss.dql.ast.Select
import com.github.zwrss.dql.parser.SelectParser
import com.github.zwrss.finder.{Source => FSource}
import com.github.zwrss.meta.MetaFactory
import play.api.libs.json.Json

import scala.io.Source

trait DBRunner {

  lazy val parser = new SelectParser {
    def parse(s: String): Select = parseAll(Select, s) match {
      case Success(result, _) => result
      case NoSuccess(message, _) => sys.error(s"Cannot parse [$s]: " + message)
    }
  }

  lazy val configuration = Json.parse({
    val source = Source.fromFile("config.json")
    try {
      source.mkString
    } finally {
      source.close()
    }
  }).as[Configuration]

  lazy val sources: Map[String, FSource[_]] = MetaFactory.create(configuration.sources)

  lazy val completeSources = sources.updated("sources", SourcesSource(sources))

}