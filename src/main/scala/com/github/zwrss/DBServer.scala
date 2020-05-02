package com.github.zwrss

import com.github.zwrss.finder.Source
import com.github.zwrss.server.Server
import com.github.zwrss.server.controller.DBController
import com.github.zwrss.server.controller.ResourcesController
import com.github.zwrss.util.PropertyStore

import scala.util.Try

object DBServer extends DBRunner {

  override protected def run(propertyStore: PropertyStore, sources: Map[String, Source[_]]): Unit = {

    val port = propertyStore.get[Int]("FINDER_ADAPTER_PORT", "port", 6666)(x => Try(x.toInt).toOption)

    val server = new Server(port = port, controllers = Seq(
      new ResourcesController("/public/*"),
      new DBController(sources)
    ))

    server.start()

    println(s"Server started on port $port")

  }

}
