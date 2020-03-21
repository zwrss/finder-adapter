package com.github.zwrss.dql.parser

import com.github.zwrss.dql.ast.Terms


/**
 * <Terms> ::= count from <ident> [where <Expression>]
 */
trait TermsParser extends SelectorParser with CriterionParser {

  final protected def Terms: Parser[Terms] =
    ("terms" ~> Selector) ~
      ("from" ~> ident) ~
      opt("where" ~> Expression) ^^ {
      case selector ~ from ~ query => new Terms(selector, from, query)
    }

}
