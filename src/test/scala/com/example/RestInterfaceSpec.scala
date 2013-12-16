package com.example

import org.scalatest.{WordSpec, Matchers}

import spray.testkit.ScalatestRouteTest
import spray.http._
import StatusCodes._
import akka.actor.Actor

class RestInterfaceSpec extends WordSpec
    with Matchers
    with ScalatestRouteTest
    with RestApi {

  def actorRefFactory = system

  "RestInterface" should {
    "return pong for GET requests to ping" in {
      Get("/ping") ~> routes ~> check {
        responseAs[String] should include ("pong")
      }
    }

    "return a 'future pong' response for GET requests to /pingfuture" in {
      Get("/pingfuture") ~> routes ~> check {
        responseAs[String] === "future pong" }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(routes) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }

  }
}
