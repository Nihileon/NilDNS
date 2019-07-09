package top.nihil.nildns

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.io.{IO, Tcp, Udp}
import akka.io.Tcp._
import akka.io.Udp._
import akka.util.ByteString

class Server(localSocket:InetSocketAddress) extends Actor {

  import context.system

  IO(Udp) ! Udp.Bind(self, localSocket)

  def receive = {
    case Udp.Bound(local) =>
      context.become(ready(sender()))
      println("bound successfully")
  }

  def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) =>
      val processed = socket ! Udp.Send(data, remote) // example server echoes back
      println("receive some message")
    case Udp.Unbind =>
      socket ! Udp.Unbind
    case Udp.Unbound =>
      context.stop(self)
  }
}
