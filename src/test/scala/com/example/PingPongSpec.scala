package com.example

import org.scalatest.{WordSpecLike,Matchers}
import akka.testkit.{ImplicitSender, TestKit}
import akka.actor.{Props, ActorSystem}


class PingPongSpec extends TestKit(ActorSystem("testPingPong"))
                         with WordSpecLike
                         with Matchers
                         with ImplicitSender
                         with StopSystemAfterAll {
  "PingPong" must {
    "Return a PongResponse when sent a PongRequest" in {
      import PingPongProtocol._

      val pingPongActor = system.actorOf(Props[PingPong])

      pingPongActor ! PongRequest
      expectMsg(PongResponse("pong"))
    }
  }
}

