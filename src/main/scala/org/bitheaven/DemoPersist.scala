package org.bitheaven

import akka.actor.ActorSystem
import org.bitheaven.messaging.Messages.{StartDemoPersist, StartDemoSimple}
import org.bitheaven.messaging.actors.Demo


object DemoPersist {
  def main(args: Array[String]): Unit = {
      val system = ActorSystem("cqrsCacheAkka")
      val demo = system.actorOf(Demo.props, "demoPersistActor")
      demo ! StartDemoPersist
  }
}
