package org.bitheaven.messaging.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import org.bitheaven.Models.{CacheConfig, Event}
import org.bitheaven.messaging.Messages.{FromCache, GetCache, MissCache, PutCache}

import swaydb._
import swaydb.serializers.Default._
import org.bitheaven.persistance.SerDe._


class CacheWorker(replyTo: ActorRef) extends Actor with ActorLogging{
  val db = persistent.Map[Long, Event](dir = getCachePath).get

  def getCachePath = s"${CacheConfig.persistBaseDir}/${self.path.name}"

  override def receive: Receive = {
    case GetCache(id) => {
      log.info(s"Cache worker got cache request for id = $id")
      getCache(id) match {
        case Some(ev) => replyTo ! FromCache(ev)
        case None => replyTo ! MissCache(id)
      }
    }

    case PutCache(id, event) => {
      log.info(s"Cache worker got PUT request for $event")
      putCache(id, event)
    }
  }

  def getCache(id:Long) = db.get(id).get

  def putCache(id:Long, event: Event) = db.put(id, event).get

}


object CacheWorker{
  def props(replyTo:ActorRef) = Props(classOf[CacheWorker], replyTo)
}