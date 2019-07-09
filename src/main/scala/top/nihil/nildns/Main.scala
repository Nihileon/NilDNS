package top.nihil.nildns

import java.net.InetSocketAddress

import akka.actor.{ActorSystem, Props}

object Main {

  def main(args: Array[String]): Unit = {
    val udpServer = ActorSystem() actorOf Props(
      new Server(
        new InetSocketAddress("0.0.0.0", 8080)))
  }

}
