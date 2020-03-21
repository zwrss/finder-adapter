package com.github.zwrss.dql.parser

import com.github.zwrss.dql.ast.LimitAST
import com.github.zwrss.dql.ast.Select


/**
 * <Select>   ::= select <Selector> from <ident> [where <Expression>] [order by <Sort>] [limit Limit]
 * <Selector> ::= <Fields> | "*"
 * <Fields>   ::= <ident> [, <ident>]*
 * <Limit>    ::= [<number>,] <number>
 */
trait SelectParser extends SelectorParser with CriterionParser with SortParser {

  final protected def Select: Parser[Select] =
    ("select" ~> Selector) ~
      ("from" ~> ident) ~
      opt("where" ~> Expression) ~
      opt("order" ~> "by" ~> Sort) ~
      opt("limit" ~> Limit) ^^ {
      case selector ~ from ~ query ~ sort ~ limit => new Select(selector, from, query, sort, limit)
    }

  private def Limit = opt(wholeNumber <~ ",") ~ wholeNumber ^^ {
    case offset ~ limit => LimitAST(offset.map(_.toLong), limit.toLong)
  }

}
