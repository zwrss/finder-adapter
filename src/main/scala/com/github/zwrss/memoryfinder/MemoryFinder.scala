package com.github.zwrss.memoryfinder

import com.github.zwrss.finder.Finder
import com.github.zwrss.finder.field.Field
import com.github.zwrss.finder.message.CountRequest
import com.github.zwrss.finder.message.CountResponse
import com.github.zwrss.finder.message.FindRequest
import com.github.zwrss.finder.message.FindResponse
import com.github.zwrss.finder.message.TermsBucket
import com.github.zwrss.finder.message.TermsRequest
import com.github.zwrss.finder.message.TermsResponse
import com.github.zwrss.finder.message.TermsValue

class MemoryFinder[Id, Entity](objects: Seq[Entity], idGetter: Entity => Id) extends Finder[Id, Entity] {

  final override def get(id: Id): Entity = objects.find(o => idGetter(o) == id) getOrElse sys.error(s"Cannot find object with id $id")

  override def find(request: FindRequest[Entity]): FindResponse[Entity] = {
    var result = objects
    request.query.foreach(q => result = result filter q.ok)
    request.sort.foreach(s => result = result sorted s.ordering)
    FindResponse(result.size.toLong, result.slice(request.offset.toInt, request.offset.toInt + request.limit.toInt))
  }

  override def count(request: CountRequest[Entity]): CountResponse = {
    CountResponse(objects count (o => request.query forall (_ ok o)))
  }

  override def terms(request: TermsRequest[Entity]): TermsResponse[Entity] = {
    val elements = objects.filter(o => request.query.forall(_ ok o))
    val buckets: Seq[TermsBucket[Entity, Any]] = request.fields.map { field =>
      val _f = field.asInstanceOf[Field[Entity, Any]]
      implicit val _o: Ordering[Any] = _f._getOrdering
      var result = Map.empty[Any, Int]
      elements.foreach { element =>
        _f.get(element).foreach { value =>
          result = result.updated(value, result.getOrElse(value, 0) + 1)
        }
      }
      val values = result.map {
        case (key, count) =>
          TermsValue[Any](key, _f.serialize(key), count)
      }(scala.collection.breakOut)
      TermsBucket[Entity, Any](_f, values.sortBy(v => (-v.count, v.key)))
    }
    TermsResponse[Entity](buckets)
  }

}