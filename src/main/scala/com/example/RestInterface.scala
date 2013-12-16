package com.example

import akka.actor.{Actor,ActorLogging,PoisonPill,Props}
import spray.routing._
import spray.http._
import MediaTypes._
import spray.can.Http
import scala.concurrent.duration._
import akka.util.Timeout
import spray.routing.RequestContext
import spray.http.StatusCodes


// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class RestInterface extends Actor with RestApi with ActorLogging {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  def receive = runRoute(routes)
}


// this trait defines our service behavior independently from the service actor
trait RestApi extends HttpService {
  import akka.pattern.ask
  import akka.pattern.pipe
  import PingPongProtocol._

  // we use the enclosing ActorContext's or ActorSystem's dispatcher for our Futures and Scheduler
  implicit def executionContext = actorRefFactory.dispatcher

  val pingPong = actorRefFactory.actorOf(Props[PingPong])
  implicit val timeout = Timeout(10.seconds)

  val routes =
    path("") {
      get {
        // XML is marshalled to `text/xml` by default, so we simply override here
        respondWithMediaType(`text/html`) {
          complete(indexContent())
        }
      }
    } ~
    path("ping") {
      get { requestContext =>
        val responder = createPingPongResponder(requestContext)
        pingPong.ask(PongRequest).pipeTo(responder)
      }
    } ~
  path("pingfuture") {
      get { requestContext =>
        val responder = createPingPongResponder(requestContext)
        pingPong.ask(PongFutureRequest).pipeTo(responder)
      }
    } ~
    path("pang") {
      get {
        complete("PANG!")
      }
    }


  def indexContent() = {
    <html>
      <body>
        <h1>Say hello to <i>spray-routing</i> on <i>spray-can</i>!</h1>
      </body>
    </html>
  }

  def createPingPongResponder(requestContext:RequestContext) = {
    actorRefFactory.actorOf(Props(new PingPongResponder(requestContext)))
  }
}



class PingPongResponder(requestContext:RequestContext) extends Actor with ActorLogging {
  import PingPongProtocol._
  import spray.httpx.SprayJsonSupport._

  def receive = {
    case PongResponse(message) =>
      requestContext.complete(StatusCodes.OK, message)
      self ! PoisonPill
    case _ =>
      log.error("received unknown PingPong message")
      requestContext.complete(StatusCodes.InternalServerError)
      self ! PoisonPill
  }
}
