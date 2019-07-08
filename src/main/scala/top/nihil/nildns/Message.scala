package top.nihil.nildns

import java.nio.ByteOrder

import akka.util.{ByteIterator, ByteString, ByteStringBuilder}

case class Message(header: Header,
                   question: Seq[Question] = Seq.empty,
                   answers: Seq[ResourceRecord] = Seq.empty,
                   authorities: Seq[ResourceRecord] = Seq.empty,
                   additions: Seq[ResourceRecord] = Seq.empty) {

  implicit val byteOrder = ByteOrder BIG_ENDIAN

  def write(res: ByteStringBuilder): Unit = {
    header write res
    question foreach (_ write res)
    answers foreach (_ write res)
    authorities foreach (_ write res)
    additions foreach (_ write res)
  }

  def write(): ByteString = {
    val res = ByteString.newBuilder
    write(res)
    res result
  }

}

object Message {

  def parse(msg: ByteString): Message = {
    val iter = msg iterator
    val header = Header parse iter
    val QDs = (0 until header.QDCOUNT) map { _ => Question parse(iter, msg) }
    val ANs = (0 until header.ANCOUNT) map { _ => ResourceRecord parse(iter, msg) }
    val NSs = (0 until header.NSCOUNT) map { _ => ResourceRecord parse(iter, msg) }
    val ARs = (0 until header.ARCOUNT) map { _ => ResourceRecord parse(iter, msg) }
    new Message(header, QDs, ANs, NSs, ARs)
  }

}
