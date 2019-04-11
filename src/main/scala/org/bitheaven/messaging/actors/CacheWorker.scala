package org.bitheaven.messaging.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import org.bitheaven.Models.{CacheConfig, Event}
import org.bitheaven.messaging.Messages.{FromCache, GetCache, MissCache, PutCache}

import swaydb._
import swaydb.serializers.Default._
import org.bitheaven.persistance.SerDe._

/** CacheWorker represents cache logic as Actor backed by SwayDB persistance
  * @todo rethink put/get vs AkkaStreams mat for sink/source
  * */
class CacheWorker(replyTo: ActorRef) extends Actor with ActorLogging{
  /* cache persistance backend */
  /** @todo Cache levels config tu be tuned into in-mem with thresh-driven persist,
    *       also segment tuning
    *       also sway paralelism tuning vs actor paralelism
    * @see http://www.swaydb.io/configuring-levels/
    * */
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

  /** Get event from cache */
  def getCache(id:Long) = db.get(id).get

  /** Put event into cache */
  def putCache(id:Long, event: Event) = db.put(id, event).get

}


object CacheWorker{
  def props(replyTo:ActorRef) = Props(classOf[CacheWorker], replyTo)
}