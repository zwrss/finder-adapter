package com.github.zwrss

import com.github.zwrss.finder.Source
import com.github.zwrss.finder.criteria.CriterionDescriptor
import com.github.zwrss.finder.sort.SortDescriptor
import com.github.zwrss.impl.memory.field.MemoryFieldDescriptor
import com.github.zwrss.impl.memory.field.SimpleFieldDescriptor
import com.github.zwrss.impl.memory.field.StringField
import com.github.zwrss.impl.memory.finder.MemorySource


object SourcesSource {

  type SourceInfo = (String, Source[_])

  def apply(sources: Map[String, Source[_]]): MemorySource[SourceInfo] = new MemorySource[SourceInfo] {

    override protected lazy val fields: Seq[MemoryFieldDescriptor[SourceInfo, _]] = Seq(
      new SimpleFieldDescriptor[SourceInfo, String]("name", _._1) with StringField[SourceInfo],
      new SimpleFieldDescriptor[SourceInfo, String]("tpe", _._2.tpe) with StringField[SourceInfo]
    )

    override protected def objects: Seq[SourceInfo] = sources.toSeq

  }
}

object SortsSource {

  type SortInfo = (String, Source[_], SortDescriptor[_])

  def apply(sources: Map[String, Source[_]]): MemorySource[SortInfo] = new MemorySource[SortInfo] {

    override protected lazy val fields: Seq[MemoryFieldDescriptor[SortInfo, _]] = Seq(
      new SimpleFieldDescriptor[SortInfo, String]("name", _._3.name) with StringField[SortInfo],
      new SimpleFieldDescriptor[SortInfo, String]("source", _._1) with StringField[SortInfo]
    )

    override protected def objects: Seq[SortInfo] = sources.toSeq.flatMap { source =>
      val x: Seq[SortInfo] = source._2.sortDescriptors.map { sort =>
        (source._1, source._2, sort)
      }
      x
    }

  }
}

object CriteriaSource {

  type CriterionInfo = (String, Source[_], CriterionDescriptor[_, _])

  def apply(sources: Map[String, Source[_]]): MemorySource[CriterionInfo] = new MemorySource[CriterionInfo] {

    override protected lazy val fields: Seq[MemoryFieldDescriptor[CriterionInfo, _]] = Seq(
      new SimpleFieldDescriptor[CriterionInfo, String]("name", _._3.name) with StringField[CriterionInfo],
      new SimpleFieldDescriptor[CriterionInfo, String]("tpe", _._3.tpe) with StringField[CriterionInfo],
      new SimpleFieldDescriptor[CriterionInfo, String]("source", _._1) with StringField[CriterionInfo]
    )

    override protected def objects: Seq[CriterionInfo] = sources.toSeq.flatMap { source =>
      val x: Seq[CriterionInfo] = source._2.criteriaDescriptors.map { criterion =>
        (source._1, source._2, criterion)
      }
      x
    }

  }
}
