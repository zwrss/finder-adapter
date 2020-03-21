package com.github.zwrss.finder

import com.github.zwrss.finder.field.Field

trait FieldsDescriptor[Entity] {

  def fields: Seq[Field[Entity, _]]

  def _getField(name: String): Field[Any, Any] = {
    fields.find(_.name == name).getOrElse(sys.error(s"No field with name $name")).asInstanceOf[Field[Any, Any]]
  }

}
