package com.github.zwrss.util

class PropertyStore(args: Array[String]) {

  def findString(envProperty: String, argProperty: String): Option[String] = {
    val argPattern: String = s"--$argProperty="
    val arg: Option[String] = args.collectFirst {
      case x if x startsWith argPattern => x stripPrefix argPattern
    }

    def env: Option[String] = sys.env.get(envProperty)

    arg orElse env
  }

  def findString(property: String): Option[String] = findString(property, property)

  def getString(envProperty: String, argProperty: String, default: => String): String =
    findString(envProperty, argProperty) getOrElse default

  def getString(property: String, default: => String): String = getString(property, property, default)

  def find[T](envProperty: String, argProperty: String)(mapper: String => Option[T]): Option[T] = {
    findString(envProperty, argProperty) flatMap mapper
  }

  def get[T](envProperty: String, argProperty: String, default: => T)(mapper: String => Option[T]): T = {
    find(envProperty, argProperty)(mapper) getOrElse default
  }

}