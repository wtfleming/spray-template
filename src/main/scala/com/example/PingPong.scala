package com.example

import akka.actor._
import concurrent.Future
import scala.concurrent.duration._
import akka.util.Timeout

class PingPong extends Actor with ActorLogging {
  import PingPongProtocol._
  import context._
  implicit val timeout = Timeout(5.seconds)

    def receive = {
      case PongRequest =>
        sender ! PongResponse("pong")
      case PongFutureRequest =>
        val capturedSender = sender
        Future {
          capturedSender ! PongResponse("future pong")
        }
      case _ =>
        log.error("received unknown message")
    }
}


object PingPongProtocol {
  import spray.json._
  import DefaultJsonProtocol._

  case object PongRequest
  case object PongFutureRequest
  case class PongResponse(message:String)

  //----------------------------------------------
  // JSON
  //----------------------------------------------
  object PongResponse extends DefaultJsonProtocol {
     implicit val format = jsonFormat1(PongResponse.apply)
   }

}

