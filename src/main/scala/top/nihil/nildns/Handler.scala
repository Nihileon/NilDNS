package top.nihil.nildns

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, Stash}
import akka.io.Udp

class Handler(ns: InetSocketAddress, upstream: ActorRef) extends Actor with ActorLogging with Stash {
  override def receive = {
    case Udp.Bound(local) =>
      log debug s"bound to UDP address $local"
      context become ready(sender())
      unstashAll()
    case _:QuestionIPV4=>
      stash()
    case _:QuestionIPV6=>
      stash()
  }

  def ready(socket: ActorRef): Receive = {
    case QuestionIPV4(id, name) =>
      log debug s"resolving $name (A)"
      val msg = Message(
        Header(id, Flags.get(RD = true), QDCOUNT = 1),
        Seq(Question(name, RecordType.A, RecordClass.IN)))
      socket ! Udp.Send(msg.write(), ns)

    case Udp.Received(data, remote) =>
      log debug s"receive remote data from$remote"
      val msg = Message parse data
      log debug s"parse $msg"
      if (msg.header.flags.rCode == RCODE.NO_ERROR)
        upstream ! Answer(msg.header.ID, msg.answers)
      else
        upstream ! Answer(msg.header.ID, Seq())
  }
}


case class QuestionIPV4(id: Int, name: String)

case class QuestionIPV6(id: Int, name: String)

case class Answer(id: Int, RRs: Iterable[ResourceRecord])
