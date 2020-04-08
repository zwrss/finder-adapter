package com.github.zwrss.finder.sort

trait SortsDescriptor[Entity] {

  def sortDescriptors: Seq[SortDescriptor[Entity]]

  def _getSortDescriptor(name: String): SortDescriptor[Any] = {
    sortDescriptors.find(_.name == name).getOrElse(sys.error(s"No sort with name $name")).asInstanceOf[SortDescriptor[Any]]
  }

}
