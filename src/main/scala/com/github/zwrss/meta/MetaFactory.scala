package com.github.zwrss.meta

import com.github.zwrss.finder.FieldsDescriptor
import com.github.zwrss.finder.Finder

object MetaFactory {

  def create(descriptors: Seq[SourceDescriptor]): Map[String, Finder[_, _] with FieldsDescriptor[_]] = descriptors.map { d =>
    d.name -> d._finder
  }(scala.collection.breakOut)

}

