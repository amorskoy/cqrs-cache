package org.bitheaven

object Models {

  /* So case pattern could be introduced on future, only if multitype cache will be needed */
  abstract class DataRow(id:Long)
  case class Event(id:Long, event_is:Long, stage:String, dataset:String) extends  DataRow(id)

  object CQRS{
    val getPoolSize = 5

    val persistDir = "/tmp/distcache"
  }
}
