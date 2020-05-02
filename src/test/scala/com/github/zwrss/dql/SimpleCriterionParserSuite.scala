package com.github.zwrss.dql

import com.github.zwrss.dql.ast._
import com.github.zwrss.dql.parser.SimpleCriterionParser
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class SimpleCriterionParserSuite extends FlatSpec with Matchers {

  private val parser = new SimpleCriterionParser {

    override protected def Select: Parser[Select] = ???

    def parse(s: String): CriterionAST = parseAll(Criterion, s) match {
      case Success(result, _) => result
      case NoSuccess(message, _) => sys.error(s"Cannot parse [$s]: " + message)
    }

  }

  import parser._

  behavior of "SimpleCriterionParser"

  it should "parse simple criteria" in {
    parse("Fuel = \"Petrol\"") shouldBe EqualsAST("Fuel", "Petrol")
    parse("Fuel in (\"Petrol\", \"Diesel\")") shouldBe InAST("Fuel", Left(List("Petrol", "Diesel")))
    parse("Power >= 100") shouldBe RangeAST("Power", min = Option("100"))
    parse("Power <= 200") shouldBe RangeAST("Power", max = Option("200"))
    parse("Power between 100 and 200") shouldBe RangeAST("Power", Option("100"), Option("200"))
    parse("Power is null") shouldBe NotAST(ExistsAST("Power"))
    parse("Fuel is not null") shouldBe ExistsAST("Fuel")
  }

}

