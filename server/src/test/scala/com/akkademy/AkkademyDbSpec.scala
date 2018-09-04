package com.akkademy

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.Timeout
import com.akkademy.messages.{AkkademyRequest, _}
import org.scalatest.{BeforeAndAfterEach, FunSpecLike, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class AkkademyDbSpec extends FunSpecLike with Matchers with BeforeAndAfterEach {

  implicit val system: ActorSystem = ActorSystem()
  implicit val timeout: Timeout = Timeout(2 seconds)

  describe("akkademy-db") {

    val key = "key"
    val value = "value"

    describe("- given SetRequest") {
      it("should place key/value into map") {

        tellEmptyDb(SetRequest(key, value)) { db =>

          db.map.get(key) should equal(Some(value))
        }
      }
    }

    describe("- given SetIfNotExistsRequest") {

      it("should place key/value into map, when the pair doesn't exist") {

        tellEmptyDb(SetIfNotExistsRequest(key, value)) { db =>

          db.map.get(key) should equal(Some(value))
        }
      }
    }

    describe("- given DeleteRequest") {

      it("should delete the pair") {

        tellPopulatedDb(key, value, DeleteRequest(key)) { db =>

          db.map.get(key) should equal(None)
        }
      }
    }

    describe("- given GetRequest") {

      it("should return the pair, when it exists") {
        val actorRef = TestActorRef(new AkkademyDb)
        val akkademyDb = actorRef.underlyingActor

        actorRef ! SetRequest(key, value)
        akkademyDb.map.get(key) should be(Some(value))

        val response = (actorRef ? GetRequest(key)).mapTo[Object]
        val result = Await.result(response, 1 second)

        result should be(value)
      }

      it("should return a failure with key not found, when the pair doesn't exist") {
        val actorRef = TestActorRef(new AkkademyDb)
        val akkademyDb = actorRef.underlyingActor

        akkademyDb.map.get(key) should be(None)

        val response = (actorRef ? GetRequest(key)).mapTo[Object]
        intercept[KeyNotFoundException] {
          Await.result(response, 1 second)
        }
      }
    }
  }

  private def tellEmptyDb[A <: AkkademyRequest](m: A)(f: AkkademyDb => Unit): Unit = {
    val actorRef = TestActorRef(new AkkademyDb)
    val akkademyDb = actorRef.underlyingActor
    akkademyDb.map.keySet.size should be(0)

    actorRef ! m

    f(akkademyDb)
  }

  private def tellPopulatedDb[A <: AkkademyRequest](k: String, v: Object, m: A)(f: AkkademyDb => Unit): Unit = {
    val actorRef = TestActorRef(new AkkademyDb)
    val akkademyDb = actorRef.underlyingActor
    akkademyDb.map.put(k, v)

    actorRef ! m

    f(akkademyDb)
  }

}
