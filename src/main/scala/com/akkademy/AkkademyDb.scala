package com.akkademy

import akka.actor.Actor
import akka.event.Logging
import com.akkademy.messages.SetRequest

import scala.collection.mutable

class AkkademyDb extends Actor {

  val map = new mutable.HashMap[String, Object]

  val logger = Logging(context.system, this)

  override def receive = {
    case SetRequest(key, value) => {
      logger.info("Received SetRequest - key: {} value: {}", key, value)
      map.put(key, value)
    }
    case o => logger.info("Received unknown object {}", o)
  }

}
