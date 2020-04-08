package com.github.zwrss.impl.memory.sort

import com.github.zwrss.finder.sort.Sort

trait MemorySort[Entity] extends Sort[Entity] {

  def ordering: Ordering[Entity]

  override def and(that: Sort[Entity]): Sort[Entity] = ComplexSort(this, that.asInstanceOf[MemorySort[Entity]])

}
