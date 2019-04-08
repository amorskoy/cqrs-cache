package org.bitheaven.messaging.actors

import java.io.File

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import org.bitheaven.Models.{CQRS, Event}
import org.bitheaven.messaging.Messages.{FromCache, GetCache, MissCache, PutCache}

import scala.collection.mutable

class CacheWorker(replyTo: ActorRef) extends Actor with ActorLogging{
  val cache = mutable.HashMap[Long, Event]()
  new File(s"${CQRS.persistDir}/${self.path.name}").mkdirs()

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

  def getCache(id:Long) = cache.get(id)

  def putCache(id:Long, event: Event) = {
    cache.put(id, event)
  }
}


object CacheWorker{
  def props(replyTo:ActorRef) = Props(classOf[CacheWorker], replyTo)
}