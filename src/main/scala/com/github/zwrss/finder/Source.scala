package com.github.zwrss.finder

import com.github.zwrss.finder.criteria.CriteriaDescriptor
import com.github.zwrss.finder.field.FieldsDescriptor
import com.github.zwrss.finder.sort.SortsDescriptor

trait Source[Entity] extends Finder[Entity] with FieldsDescriptor[Entity]
  with CriteriaDescriptor[Entity] with SortsDescriptor[Entity] {
  def tpe: String
}
