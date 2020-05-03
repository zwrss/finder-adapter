package com.github.zwrss.dql.ast

case class JoinAST(source: String, left: String, right: String, limit: Option[Long])