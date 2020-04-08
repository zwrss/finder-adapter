package com.github.zwrss.server.controller

import com.github.zwrss.server.HttpMethod

import scala.io.Source


class ResourcesController(route: String) extends Controller {
  endpoint(route) {
    case (HttpMethod.GET, request, response) =>
      val file = request.getRequestURI.stripPrefix("/")
      val resource = Source.fromResource(file)
      response setStatus 200
      resource.getLines foreach response.getWriter.println
  }
}