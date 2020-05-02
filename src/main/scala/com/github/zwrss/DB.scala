package com.github.zwrss

import com.github.zwrss.console.TablePrinter
import com.github.zwrss.dql.ast.DqlCommand
import com.github.zwrss.finder.Source
import com.github.zwrss.util.PropertyStore

import scala.io.StdIn


object DB extends DBRunner {

  override protected def run(propertyStore: PropertyStore, sources: Map[String, Source[_]]): Unit = {

    var command = ""

    while (command != "exit") {
      command = StdIn.readLine("Insert command:\n")
      if (command != "exit") {
        val toPrint = try {
          val dqlCommand = DqlCommand.fromDql(command)
          val dqlResult = dqlCommand.execute(sources)
          TablePrinter.format(dqlResult.headers, dqlResult.values)
        } catch {
          case t: Throwable =>
            TablePrinter.format(List("error"), List(List(t.getMessage)))
        }
        println(toPrint)
      }
    }

    System.exit(0)

  }

}
