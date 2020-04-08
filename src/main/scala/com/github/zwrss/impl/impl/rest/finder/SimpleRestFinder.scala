package com.github.zwrss.impl.impl.rest.finder

import com.github.zwrss.finder.Finder
import com.github.zwrss.finder.message.CountRequest
import com.github.zwrss.finder.message.CountResponse
import com.github.zwrss.finder.message.FindRequest
import com.github.zwrss.finder.message.FindResponse
import com.github.zwrss.finder.message.TermsRequest
import com.github.zwrss.finder.message.TermsResponse
import com.github.zwrss.http.JsonHttpClient
import com.github.zwrss.impl.impl.rest.criteria.RCriterion
import com.github.zwrss.impl.impl.rest.sort.RSort
import com.github.zwrss.util.MapMerge
import play.api.libs.json.JsValue
import play.api.libs.json.Reads

abstract class SimpleRestFinder[Entity: Reads] extends Finder[Entity] {

  protected def http: JsonHttpClient

  protected def url: String

  protected def itemsPath: String

  protected def totalPath: String

  override def find(request: FindRequest[Entity]): FindResponse[Entity] = {

    val queryParameters: Map[String, Seq[String]] = {
      val fromCriteria: Map[String, Seq[String]] = request.query match {
        case Some(c: RCriterion) => c.parameters
        case _ => Map.empty
      }
      val fromSorts: Map[String, Seq[String]] = request.sort match {
        case Some(s: RSort) => s.parameters
        case _ => Map.empty
      }
      MapMerge.merge(fromCriteria, fromSorts)
    }

    val response = http.get(url, queryParameters)
    val total: Long = totalPath.split('.').foldLeft(response) {
      case (obj, path) => (obj \ path).as[JsValue]
    }.asOpt[Long].getOrElse(0l)
    val items: Seq[Entity] = itemsPath.split('.').foldLeft(response) {
      case (obj, path) => (obj \ path).as[JsValue]
    }.as[Seq[Entity]]
    FindResponse(total, items)
  }

  override def count(request: CountRequest[Entity]): CountResponse = ???

  override def terms(request: TermsRequest[Entity]): TermsResponse[Entity] = ???

}