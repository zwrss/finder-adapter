package com.github.zwrss.impl.memory.finder

import com.github.zwrss.finder.Finder
import com.github.zwrss.finder.field.FieldDescriptor
import com.github.zwrss.finder.message.CountRequest
import com.github.zwrss.finder.message.CountResponse
import com.github.zwrss.finder.message.FindRequest
import com.github.zwrss.finder.message.FindResponse
import com.github.zwrss.finder.message.TermsBucket
import com.github.zwrss.finder.message.TermsRequest
import com.github.zwrss.finder.message.TermsResponse
import com.github.zwrss.finder.message.TermsValue
import com.github.zwrss.impl.memory.criteria.MemoryCriterion
import com.github.zwrss.impl.memory.sort.MemorySort

abstract class MemoryFinder[Entity] extends Finder[Entity] {

  protected def objects: Seq[Entity]

  override def find(request: FindRequest[Entity]): FindResponse[Entity] = {
    var result: Seq[Entity] = objects
    request.query.foreach {
      case q: MemoryCriterion[_] => result = result filter q.forceOk
      case _ => result = Seq.empty
    }
    request.sort.foreach {
      case s: MemorySort[_] => result = result sorted s.ordering
      case _ => // do nothing
    }
    FindResponse(result.size.toLong, result.slice(request.offset.toInt, request.offset.toInt + request.limit.toInt))
  }

  override def count(request: CountRequest[Entity]): CountResponse = {
    CountResponse(objects count (o => request.query forall {
      case q: MemoryCriterion[_] => q forceOk o
      case _ => false
    }))
  }

  override def terms(request: TermsRequest[Entity]): TermsResponse[Entity] = {
    val elements = objects.filter(o => request.query.forall {
      case q: MemoryCriterion[_] => q forceOk o
      case _ => false
    })
    val buckets: Seq[TermsBucket[Entity, Any]] = request.fields.map { field =>
      val _f = field.asInstanceOf[FieldDescriptor[Entity, Any]]
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
      TermsBucket[Entity, Any](_f, values /*.sortBy(v => (-v.count, v.key))*/)
    }
    TermsResponse[Entity](buckets)
  }

}