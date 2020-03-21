package com.github.zwrss

import com.github.zwrss.console.TablePrinter
import com.github.zwrss.dql.ast.DqlCommand
import com.github.zwrss.dql.ast.Select
import com.github.zwrss.dql.parser.SelectParser
import com.github.zwrss.finder.FieldsDescriptor
import com.github.zwrss.finder.Finder
import com.github.zwrss.meta.MetaFactory
import play.api.libs.json.Json

import scala.io.Source
import scala.io.StdIn

object DB {

  def main(args: Array[String]): Unit = try {

    var command = ""

    val parser = new SelectParser {
      def parse(s: String): Select = parseAll(Select, s) match {
        case Success(result, _) => result
        case NoSuccess(message, _) => sys.error(s"Cannot parse [$s]: " + message)
      }
    }

    val configuration = Json.parse({
      val source = Source.fromFile("config.json")
      try {
        source.mkString
      } finally {
        source.close()
      }
    }).as[Configuration]

    val finders: Map[String, Finder[_, _] with FieldsDescriptor[_]] = MetaFactory.create(configuration.sources)

    while (command != "exit") {
      command = StdIn.readLine("Insert command:\n")
      if (command != "exit") {
        val toPrint = try {
          val dqlCommand = DqlCommand.fromDql(command)
          val dqlResult = dqlCommand.execute(finders)
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
