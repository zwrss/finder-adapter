package com.github.zwrss.dql.parser

import com.github.zwrss.dql.ast.Count


/**
 * <Count> ::= count from <ident> [where <Expression>]
 */
trait CountParser extends CriterionParser {

  final protected def Count: Parser[Count] =
    "count" ~> "from" ~> ident ~ opt("where" ~> Expression) ^^ {
      case from ~ query => new Count(from, query)
    }

}
