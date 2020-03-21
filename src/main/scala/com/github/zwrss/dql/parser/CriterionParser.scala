package com.github.zwrss.dql.parser

import com.github.zwrss.dql.ast.CriterionAST

trait CriterionParser extends SimpleCriterionParser with LogicalExpressionParser {

  final override protected def Variable: Parser[CriterionAST] = Criterion

}

