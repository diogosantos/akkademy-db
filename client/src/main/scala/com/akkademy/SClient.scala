package com.akkademy

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.akkademy.messages.{GetRequest, SetRequest}

import scala.concurrent.Future
import scala.concurrent.duration._

class SClient(remoteAddress: String) {

  private implicit val timeout: Timeout = Timeout(2 seconds)
  private implicit val actorSystem: ActorSystem = ActorSystem("LocalSystem")
  private val remoteDb = actorSystem.actorSelection(s"akka.tcp://akkademy@$remoteAddress/user/akkademy-db")

  def set(key: String, value: Object): Future[Any] = {
    remoteDb ? SetRequest(key, value)
  }

  def get(key: String): Future[Any] = {
    remoteDb ? GetRequest(key)
  }

}
