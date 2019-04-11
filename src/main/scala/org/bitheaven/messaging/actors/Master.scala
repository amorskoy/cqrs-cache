package org.bitheaven.messaging.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.routing.{ConsistentHashingPool, ConsistentHashingRoutingLogic}
import akka.routing.ConsistentHashingRouter.ConsistentHashMapping
import org.bitheaven.Models.{CacheConfig, Event}
import org.bitheaven.messaging.Messages._

/**Master is a cache facade, hiding cache complexities from uter world*/
class Master(replyTo:ActorRef) extends Actor with ActorLogging{

  val cachePool = context.actorOf(
    ConsistentHashingPool(CacheConfig.cacheWorkersPoolSize, hashMapping = hashMapping).props(CacheWorker.props(self)),
    "hashRouter"
  )

  def hashMapping: ConsistentHashMapping = {
    case ev:GetCache => ev.id
    case pev:PutCache => pev.id
  }

  val sparkWorker = context.actorOf(SparkWorker.props(self), "sparkWorker")

  override def receive: Receive = {
    case g:Get =>{
      log.info(s"Master received Get for id = ${g.id}")
      cachePool ! GetCache(g.id)
    }

    case MissCache(id) => {
      log.info(s"Missed cache for id = $id")

      /** @TODO instead if driver call per id - just accumulate miss-cache id list and do one call*/
      sparkWorker ! GetStore(Seq(id))
    }

    case FromCache(ev) => {
      log.info(s"Cache hit: $ev")
      replyTo ! ev
    }

    case FromStore(ev) => {
      log.info(s"Store hit: $ev")
      cachePool ! PutCache(ev.id, ev)
      replyTo ! ev
    }
  }
}


object Master{
  def props(replyTo:ActorRef) = Props(classOf[Master], replyTo)
}