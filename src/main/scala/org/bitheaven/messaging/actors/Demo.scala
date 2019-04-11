package org.bitheaven.messaging.actors

import akka.actor.{Actor, ActorLogging, Props}
import org.bitheaven.Models.Event
import org.bitheaven.messaging.Messages.{FinishDemo, Get, StartDemoPersist, StartDemoSimple}


class Demo extends Actor with ActorLogging{
  override def receive: Receive = {
    case StartDemoSimple => demoSimple

    case StartDemoPersist => demoPersist

    case FinishDemo => endDemo

    case event:Event => {
      log.info(s"Finally got an event: $event")
    }
  }


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

  def demoPersist = {
    import swaydb._
    import swaydb.serializers.Default._
    import org.bitheaven.persistance.SerDe._

    log.info("Starting SwayDB persistance demo")

    val db = persistent.Map[Long, Event](dir = "/tmp/persist").get

    val event = Event(1, 1524322357, "remote_to_local_copy", "pre")

    log.info(s"Putting event into SwayDB: $event")
    db.put(event.id, event).get

    log.info(s"Getting event from SwayDB by id = ${event.id}")
    val res = db.get(event.id).get
    log.info(res.toString)

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
