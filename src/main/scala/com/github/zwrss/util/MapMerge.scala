package com.github.zwrss.util

object MapMerge {

  def merge[Key, Value](m1: Map[Key, Seq[Value]], m2: Map[Key, Seq[Value]]): Map[Key, Seq[Value]] = {
    (m1.keySet ++ m2.keySet).map { key =>
      key -> (m1.getOrElse(key, Seq.empty) ++ m2.getOrElse(key, Seq.empty))
    }(scala.collection.breakOut)
  }

}
