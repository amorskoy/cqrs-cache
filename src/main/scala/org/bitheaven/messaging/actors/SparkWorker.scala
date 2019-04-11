package org.bitheaven.messaging.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import org.bitheaven.Models.Event
import org.bitheaven.messaging.Messages.{FromStore, GetStore}

/**SparkWorker is just a stub Actor, emulating actual RDD/DF/DS driver calls*/
class SparkWorker(replyTo:ActorRef) extends Actor with ActorLogging{
  override def receive: Receive = {
    case GetStore(idList) => {
      log.info(s"Spark worker got request for id list = $idList")

      /** @TODO remove fake
        * Actual Spark RDD, DF or DS logic to be here */
      replyTo ! FromStore(Event(idList(0), 1524322357, "remote_to_local_copy", "pre"))
    }
  }
}

object SparkWorker{
  def props(replyTo:ActorRef) = Props(classOf[SparkWorker], replyTo)
}
