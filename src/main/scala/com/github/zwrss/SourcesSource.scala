package com.github.zwrss

import com.github.zwrss.finder.Source
import com.github.zwrss.impl.memory.field.MemoryFieldDescriptor
import com.github.zwrss.impl.memory.field.SimpleFieldDescriptor
import com.github.zwrss.impl.memory.field.StringField
import com.github.zwrss.impl.memory.finder.MemorySource


object SourcesSource {
  def apply(sources: Map[String, Source[_]]): MemorySource[(String, Source[_])] = new MemorySource[(String, Source[_])] {

    override protected lazy val fields: Seq[MemoryFieldDescriptor[(String, Source[_]), _]] = Seq(
      new SimpleFieldDescriptor[(String, Source[_]), String]("name", _._1) with StringField[(String, Source[_])],
      new SimpleFieldDescriptor[(String, Source[_]), String]("tpe", _._2.tpe) with StringField[(String, Source[_])]
    )

    override protected def objects: Seq[(String, Source[_])] = sources.toSeq

  }
}
