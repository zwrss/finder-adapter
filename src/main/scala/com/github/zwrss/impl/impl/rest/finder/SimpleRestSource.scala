package com.github.zwrss.impl.impl.rest.finder

import com.github.zwrss.finder.Source
import play.api.libs.json.Reads

abstract class SimpleRestSource[Entity: Reads] extends SimpleRestFinder[Entity] with Source[Entity] {

  override def tpe: String = "rest"

}