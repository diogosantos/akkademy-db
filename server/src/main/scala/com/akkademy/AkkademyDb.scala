package com.akkademy

import akka.actor.{Actor, ActorSystem, Props, Status}
import akka.event.Logging
import com.akkademy.messages.{GetRequest, KeyNotFoundException, SetRequest}

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
    case GetRequest(key) => {
      logger.info("Received GetRequest - key: {}", key)
      map.get(key) match {
        case Some(v) => sender() ! Status.Success(v)
        case None => Status.Failure(KeyNotFoundException(key))
      }
    }
    case _ => Status.Failure(new ClassNotFoundException())
  }

}

object Main extends App {
  val actorSystem = ActorSystem("akkademy")
  val akkademyDb = actorSystem.actorOf(Props[AkkademyDb], name = "akkademy-db")
}
