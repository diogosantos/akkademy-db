package com.akkademy

import com.akkademy.messages.KeyNotFoundException
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class SClientIntegrationSpec extends FunSpecLike with Matchers {

  val client = new SClient("127.0.0.1:2552")

  describe("akkademyDbClient") {

    it("should set a value and get a value") {
      client.set("123", new Integer(123))
      val futureResult = client.get("123")
      val result = Await.result(futureResult, 10 seconds)
      result should equal(123)
    }

    it("should delete a value") {
      client.set("456", new Integer(456))
      val futureResult = client.delete("456")
      val result = Await.result(futureResult, 10 seconds)
      result should be(true)
    }

    it("should throw an error if a key doesn't exist") {
      val futureResult = client.get("999")

      intercept[KeyNotFoundException] {

        Await.result(futureResult, 10 seconds)
      }
    }

  }

}
