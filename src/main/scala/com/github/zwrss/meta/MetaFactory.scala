package com.github.zwrss.meta

import com.github.zwrss.finder.Source

object MetaFactory {

  def create(descriptors: Seq[SourceDefinition]): Map[String, Source[_]] = descriptors.map { d =>
    d.name -> d._source
  }.toMap

}

