package com.github.zwrss.finder.criteria

trait CriteriaDescriptor[Entity] {

  def criteriaDescriptors: Seq[CriterionDescriptor[Entity, _]]

  def _getCriterionDescriptor(name: String): CriterionDescriptor[Any, Any] = {
    criteriaDescriptors.find(_.name == name).getOrElse(sys.error(s"No criterion descriptor with name $name")).asInstanceOf[CriterionDescriptor[Any, Any]]
  }

}
