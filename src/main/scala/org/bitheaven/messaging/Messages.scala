package org.bitheaven.messaging

import org.bitheaven.Models.Event

object Messages {
  final case class Get(id:Long)
  final case class GetCache(id:Long)
  final case class PutCache(id:Long, event:Event)
  final case class GetStore(ids:Seq[Long])
  final case class FromStore(event:Event)
  final case class FromCache(event:Event)
  final case class MissCache(id:Long)

  object StartDemoSimple
  object FinishDemo
}
