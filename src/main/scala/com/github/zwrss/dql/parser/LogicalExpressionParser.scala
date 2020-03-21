package com.github.zwrss.dql.parser

import com.github.zwrss.dql.ast.CriterionAST
import com.github.zwrss.dql.ast.NotAST

/**
 * <Expression> ::= <Term> [or <Term>]*
 * <Term>       ::= <NotFactor> [and <NotFactor>]*
 * <NotFactor>  ::= [not] <Factor>
 * <Factor>     ::= <Variable> | (<Expression>)
 */
trait LogicalExpressionParser extends DqlParser {

  final protected def Expression: Parser[CriterionAST] = Term ~ rep("or" ~> Term) ^^ {
    case q1 ~ qs => (q1 /: qs) (_ || _)
  }

  private def Term: Parser[CriterionAST] = NotFactor ~ rep("and" ~> NotFactor) ^^ {
    case q1 ~ qs => (q1 /: qs) (_ && _)
  }

  private def NotFactor: Parser[CriterionAST] = opt("not") ~ Factor ^^ {
    case Some(_) ~ factor => NotAST(factor)
    case _ ~ factor => factor
  }

  private def Factor: Parser[CriterionAST] = Variable | brackets(Expression)

  protected def Variable: Parser[CriterionAST]

}