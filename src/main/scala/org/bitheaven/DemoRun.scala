package org.bitheaven

import akka.actor.ActorSystem
import org.bitheaven.messaging.Messages.StartDemoSimple
import org.bitheaven.messaging.actors.Demo


object DemoRun {
  def main(args: Array[String]): Unit = {
      val system = ActorSystem("cqrsCacheAkka")
      val demo = system.actorOf(Demo.props, "demoActor")
      demo ! StartDemoSimple
  }
}
