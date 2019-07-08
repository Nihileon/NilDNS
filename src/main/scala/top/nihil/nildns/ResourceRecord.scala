package top.nihil.nildns

import java.net.{Inet4Address, Inet6Address, InetAddress}
import java.nio.ByteOrder

import akka.util.{ByteIterator, ByteString, ByteStringBuilder}

sealed abstract class ResourceRecord(val name: String, val ttl: Int, val rType: Short, val rClass: Short) {

  def write(iter: ByteStringBuilder): Unit = {
    implicit val byteOrder = ByteOrder.BIG_ENDIAN
    DomainName write(iter, name)
    iter putShort rType
    iter putShort rClass
  }

}

case class ARecord(override val name: String, override val ttl: Int, ip: Inet4Address)
  extends ResourceRecord(name, ttl, RecordType.A.id.toShort, RecordClass.IN.id.toShort) {

  implicit val byteOrder = ByteOrder BIG_ENDIAN

  override def write(iter: ByteStringBuilder): Unit = {
    super.write(iter)
    val addr = ip.getAddress
    iter putShort addr.length
    iter putBytes addr
  }

}

object ARecord {

  def parse(name: String, ttl: Int, length: Short, iter: ByteIterator): ARecord = {
    val ip = Array.ofDim[Byte](4)
    iter getBytes ip
    ARecord(name, ttl, InetAddress.getByAddress(ip).asInstanceOf)
  }

}

case class UnknownRecord(override val name: String,
                         override val ttl: Int,
                         override val rType: Short,
                         override val rClass: Short,
                         data: ByteString) extends ResourceRecord(name, ttl, rType, rClass) {

  implicit val byteOrder = ByteOrder BIG_ENDIAN

  override def write(iter: ByteStringBuilder): Unit = {
    super.write(iter)
    iter putShort data.length
    iter append data
  }

}

object UnknownRecord {

  def parse(name: String, ttl: Int, rType: Short, rClass: Short, length: Short, iter: ByteIterator): UnknownRecord = {
    UnknownRecord(name, ttl, rType, rClass, iter.toByteString)
  }

}

object ResourceRecord {

  implicit val byteOrder = ByteOrder.BIG_ENDIAN

  def parse(iter: ByteIterator, msg: ByteString): ResourceRecord = {
    val name = DomainName parse(iter, msg)
    val rType = iter getShort
    val rClass = iter getShort
    val ttl = iter getInt
    val rdLength = iter getShort
    val data = iter clone() take rdLength
    iter drop rdLength

    rType match {
      case 1 =>
        ARecord parse(name, ttl, rdLength, data)
      case _ =>
        UnknownRecord parse(name, ttl, rType, rClass, rdLength, data)
    }
  }

}
