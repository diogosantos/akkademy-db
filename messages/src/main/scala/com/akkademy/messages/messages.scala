package com.akkademy.messages

sealed trait AkkademyRequest

case class SetRequest(key: String, value: Object) extends AkkademyRequest

case class GetRequest(key: String) extends AkkademyRequest

case class SetIfNotExistsRequest(key: String, value: Object) extends AkkademyRequest

case class DeleteRequest(key: String) extends AkkademyRequest

case class KeyNotFoundException(key: String) extends Exception