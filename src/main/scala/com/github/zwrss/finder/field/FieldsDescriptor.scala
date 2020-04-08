package com.github.zwrss.finder.field

trait FieldsDescriptor[Entity] {

  def fieldDescriptors: Seq[FieldDescriptor[Entity, _]]

  def _getFieldDescriptor(name: String): FieldDescriptor[Any, Any] = {
    fieldDescriptors.find(_.name == name).getOrElse(sys.error(s"No field descriptor with name $name")).asInstanceOf[FieldDescriptor[Any, Any]]
  }

}
