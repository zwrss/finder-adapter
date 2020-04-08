package com.github.zwrss.dql.parser

import com.github.zwrss.dql.ast.SimpleSortAST
import com.github.zwrss.dql.ast.SortAST

/**
 * <Sort>       ::= <SimpleSort> [, <SimpleSort>]*
 * <SimpleSort> ::= <Descending> | <Ascending>
 * <Descending> ::= <ident> desc
 * <Ascending>  ::= <ident>
 */
trait SortParser extends DqlParser {

  final protected def Sort: Parser[SortAST] = SimpleSort ~ rep("," ~> SimpleSort) ^^ {
    case s1 ~ ss => (s1 /: ss) (_ and _)
  }

  private def SimpleSort: Parser[SortAST] = Descending | Ascending

  private def Descending: Parser[SortAST] = ident <~ "desc" ^^ {
    case f => SimpleSortAST(f, ascending = false)
  }

  private def Ascending: Parser[SortAST] = ident ^^ {
    case f => SimpleSortAST(f, ascending = true)
  }

}
