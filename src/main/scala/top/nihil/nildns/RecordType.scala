package top.nihil.nildns

import java.nio.ByteOrder

import akka.util.{ByteIterator, ByteStringBuilder}

object RecordType extends Enumeration {

  val A = Value(1)
  val CNAME = Value(5)
  val SOA = Value(6)
  val NULL = Value(10)
  val AAAA = Value(28)

  private implicit val byteOrder = ByteOrder.BIG_ENDIAN

  def parse(iter: ByteIterator): Value = RecordType(iter.getShort)

  def write(out: ByteStringBuilder, value: Value): Unit = {
    out putShort value.id
  }

}
