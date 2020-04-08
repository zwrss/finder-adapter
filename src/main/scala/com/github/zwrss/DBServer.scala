package com.github.zwrss

import com.github.zwrss.server.Server
import com.github.zwrss.server.controller.DBController
import com.github.zwrss.server.controller.ResourcesController

import scala.util.Try

object DBServer extends DBRunner {
  def main(args: Array[String]): Unit = {

    val port = {
      args.headOption orElse sys.env.get("PORT") flatMap (a => Try(a.toInt).toOption) getOrElse 666
    }

    val server = new Server(port = port, controllers = Seq(
      new ResourcesController("/public/*"),
      new DBController(completeSources)
    ))

    server.start()

    println(s"Server started on port $port")

  }

}
