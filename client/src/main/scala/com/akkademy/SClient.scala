package com.akkademy

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.akkademy.messages.{DeleteRequest, GetRequest, SetRequest}

import scala.concurrent.Future
import scala.concurrent.duration._

class SClient(remoteAddress: String) {

  private implicit val timeout: Timeout = Timeout(2 seconds)
  private implicit val actorSystem: ActorSystem = ActorSystem("LocalSystem")
  private val remoteDb = actorSystem.actorSelection(s"akka.tcp://akkademy@$remoteAddress/user/akkademy-db")

  def set(key: String, value: Object): Future[Any] = {
    remoteDb ? SetRequest(key, value)
  }

  def get(key: String): Future[Object] = {
    (remoteDb ? GetRequest(key)).mapTo[Object]
  }

  def delete(key: String): Future[Boolean] = {
    (remoteDb ? DeleteRequest(key)).mapTo[Boolean]
  }

}
