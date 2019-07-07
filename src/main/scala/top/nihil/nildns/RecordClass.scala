package top.nihil.nildns

import java.nio.ByteOrder

import akka.util.{ByteIterator, ByteStringBuilder}

object RecordClass extends Enumeration {

  val IN = Value(1)
  val CS = Value(2)
  val CH = Value(3)
  val HS = Value(4)

  private implicit val byteOrder = ByteOrder.BIG_ENDIAN

  def parse(iter: ByteIterator): Value = RecordClass(iter.getShort)

  def write(out: ByteStringBuilder, value: Value) = Unit {
    out putShort value.id
  }
}
