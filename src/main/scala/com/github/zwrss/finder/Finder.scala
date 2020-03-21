package com.github.zwrss.finder

import com.github.zwrss.finder.message.CountRequest
import com.github.zwrss.finder.message.CountResponse
import com.github.zwrss.finder.message.FindRequest
import com.github.zwrss.finder.message.FindResponse
import com.github.zwrss.finder.message.TermsRequest
import com.github.zwrss.finder.message.TermsResponse

trait Finder[Id, Entity] {

  def get(id: Id): Entity

  def find(request: FindRequest[Entity]): FindResponse[Entity]

  def count(request: CountRequest[Entity]): CountResponse

  def terms(request: TermsRequest[Entity]): TermsResponse[Entity]

}
