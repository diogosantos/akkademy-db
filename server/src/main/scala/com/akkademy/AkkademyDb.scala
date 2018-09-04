package com.akkademy

import akka.actor.{Actor, ActorSystem, Props, Status}
import akka.event.Logging
import com.akkademy.messages._

import scala.collection.mutable

class AkkademyDb extends Actor {

  val map = new mutable.HashMap[String, Object]

  val logger = Logging(context.system, this)

  override def receive: PartialFunction[Any, Unit] = {
    case SetRequest(key, value) => {
      logger.info("Received SetRequest - key: {} value: {}", key, value)
      map.put(key, value)
      sender() ! Status.Success()
    }
    case SetIfNotExistsRequest(key, value) => {
      logger.info("Received SetIfNotExistsRequest - key: {} value: {}", key, value)
      map.get(key) match {
        case Some(_) => sender() ! Status.Success(false)
        case None => map.put(key, value); sender() ! Status.Success(true)
      }
    }
    case DeleteRequest(key) => {
      logger.info("Received DeleteRequest - key: {}", key)
      map.remove(key) match {
        case Some(_) => sender() ! Status.Success(true)
        case None => sender() ! Status.Success(false)
      }
    }
    case GetRequest(key) => {
      logger.info("Received GetRequest - key: {}", key)
      map.get(key) match {
        case Some(v) => sender() ! Status.Success(v)
        case None => sender() ! Status.Failure(KeyNotFoundException(key))
      }
    }
    case _ => Status.Failure(new ClassNotFoundException())
  }

}

object Main extends App {
  val actorSystem = ActorSystem("akkademy")
  val akkademyDb = actorSystem.actorOf(Props[AkkademyDb], name = "akkademy-db")
}
