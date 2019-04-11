package org.bitheaven.messaging.actors

import akka.actor.{Actor, ActorLogging, Props}
import org.bitheaven.Models.Event
import org.bitheaven.messaging.Messages.{FinishDemo, Get, StartDemoSimple}


/** Entry point for all demos. Just one demo in this case */
class Demo extends Actor with ActorLogging{
  override def receive: Receive = {
    case StartDemoSimple => demoSimple

    case FinishDemo => endDemo

    case event:Event => {
      log.info(s"Finally got an event: $event")
    }
  }

  /** Simple cache miss -> cache put -> cache hit demo
    * @todo spark worker uses emulated Spark logic, nothing interesting there */
  def demoSimple = {
    println("Scenario #1 - Missed cache")
    log.info("Starting simple demo")
    val master = context.system.actorOf(Master.props(self), "masterActor")
    master ! Get(1)

    Thread.sleep(2000)
    println("Scenario #2 - Cache hit")
    master ! Get(1)

    self ! FinishDemo
  }


  def endDemo = {
    log.info("Got demo finishing request")
    context.system.terminate()
  }
}

object Demo{
  def props = Props[Demo]
}
