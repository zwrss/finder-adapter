package com.github.zwrss.impl.memory.finder

import com.github.zwrss.finder.Source
import com.github.zwrss.finder.criteria.CriterionDescriptor
import com.github.zwrss.finder.field.FieldDescriptor
import com.github.zwrss.finder.sort.SortDescriptor
import com.github.zwrss.impl.memory.field.MemoryFieldDescriptor

abstract class MemorySource[Entity] extends MemoryFinder[Entity] with Source[Entity] {

  override def tpe: String = "memory"

  protected def fields: Seq[MemoryFieldDescriptor[Entity, _]]

  final override def fieldDescriptors: Seq[FieldDescriptor[Entity, _]] = fields

  final override def sortDescriptors: Seq[SortDescriptor[Entity]] = fields

  final override def criteriaDescriptors: Seq[CriterionDescriptor[Entity, _]] = fields

}
