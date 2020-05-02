package com.github.zwrss

import com.github.zwrss.finder.{Source => FSource}
import com.github.zwrss.meta.MetaFactory
import com.github.zwrss.util.PropertyStore
import play.api.libs.json.Json

import scala.io.Source

trait DBRunner {

  final def main(args: Array[String]): Unit = try {

    val propertyStore = new PropertyStore(args)
    val configuration: Configuration = Json.parse({
      val source = Source.fromFile(propertyStore.getString("FINDER_ADAPTER_CONFIG", "config", "config.json"))
      try {
        source.mkString
      } finally {
        source.close()
      }
    }).as[Configuration]
    val sources: Map[String, FSource[_]] = MetaFactory.create(configuration.sources)
    run(
      propertyStore,
      sources.
        updated("sources", SourcesSource(sources)).
        updated("criteria", CriteriaSource(sources)).
        updated("sorts", SortsSource(sources))
    )

  } catch {
    case t: Throwable =>
      t printStackTrace System.out
      System.exit(1)
  }

  protected def run(propertyStore: PropertyStore, sources: Map[String, FSource[_]]): Unit

}