package com.github.zwrss.dql.parser

import com.github.zwrss.dql.ast.JoinAST
import com.github.zwrss.dql.ast.LimitAST
import com.github.zwrss.dql.ast.Select


/**
 * <Select>   ::= select <Selector> from <ident> [join <Join>] [where <Expression>] [order by <Sort>] [limit Limit]
 * <Join>     ::= <ident> on <ident> = <ident>
 * <Selector> ::= <Fields> | "*"
 * <Fields>   ::= <ident> [, <ident>]*
 * <Limit>    ::= [<number>,] <number>
 */
trait SelectParser extends SelectorParser with CriterionParser with SortParser {

  final protected def Select: Parser[Select] =
    ("select" ~> Selector) ~
      ("from" ~> ident) ~
      opt("join" ~> Join) ~
      opt("where" ~> Expression) ~
      opt("order" ~> "by" ~> Sort) ~
      opt("limit" ~> Limit) ^^ {
      case selector ~ from ~ join ~ query ~ sort ~ limit => new Select(selector, from, join, query, sort, limit)
    }

  private def Join: Parser[JoinAST] = ident ~ ("on" ~> ident) ~ ("=" ~> ident) ^^ {
    case table ~ left ~ right => JoinAST(table, left, right)
  }

  private def Limit = opt(wholeNumber <~ ",") ~ wholeNumber ^^ {
    case offset ~ limit => LimitAST(offset.map(_.toLong), limit.toLong)
  }

}
