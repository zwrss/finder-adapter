package com.github.zwrss

import com.github.zwrss.console.TablePrinter
import com.github.zwrss.dql.ast.DqlCommand

import scala.io.StdIn


object DB extends DBRunner {

  def main(args: Array[String]): Unit = try {

    var command = ""

    while (command != "exit") {
      command = StdIn.readLine("Insert command:\n")
      if (command != "exit") {
        val toPrint = try {
          val dqlCommand = DqlCommand.fromDql(command)
          val dqlResult = dqlCommand.execute(completeSources)
          TablePrinter.format(dqlResult.headers, dqlResult.values)
        } catch {
          case t: Throwable =>
            TablePrinter.format(List("error"), List(List(t.getMessage)))
        }
        println(toPrint)
      }
    }

  } catch {
    case t: Throwable =>
      t printStackTrace System.out
  } finally {
    System.exit(0)
  }

}
