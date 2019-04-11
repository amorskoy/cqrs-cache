package org.bitheaven.messaging

import org.bitheaven.Models.Event

/** Messages represent Actor comminications */
object Messages {
  /* Master level */
  final case class Get(id:Long)

  /* Wroker level */
  final case class GetCache(id:Long)
  final case class PutCache(id:Long, event:Event)
  final case class GetStore(ids:Seq[Long])
  final case class FromStore(event:Event)
  final case class FromCache(event:Event)
  final case class MissCache(id:Long)

  /* Demo scenario level */
  object StartDemoSimple
  object FinishDemo
}
