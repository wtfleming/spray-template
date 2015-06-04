package com.example

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

import com.typesafe.config.ConfigFactory

object Boot extends App {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("my-worker-actor-system")

  // create and start our service actor
  val service = system.actorOf(Props[RestInterface], "httpinterface")

  implicit val timeout = Timeout(5.seconds)

  // start a new HTTP server with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = host, port = port)
}
