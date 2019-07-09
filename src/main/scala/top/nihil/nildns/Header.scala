package top.nihil.nildns

import java.nio.ByteOrder

import akka.util.{ByteIterator, ByteString, ByteStringBuilder}

/*
                                    1  1  1  1  1  1
      0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                      ID                       |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |QR|   Opcode  |AA|TC|RD|RA|   Z    |   RCODE   |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    QDCOUNT                    |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    ANCOUNT                    |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    NSCOUNT                    |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    ARCOUNT                    |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 */

case class Header(ID: Int, flags: Flags, QDCOUNT: Int = 0, ANCOUNT: Int = 0, NSCOUNT: Int = 0, ARCOUNT: Int = 0) {

  implicit val byteOrder = ByteOrder BIG_ENDIAN

  def write(bsb: ByteStringBuilder): Unit = {
    bsb.putShort(ID)
      .putShort(flags toShort)
      .putShort(ANCOUNT)
      .putShort(NSCOUNT)
      .putShort(ARCOUNT)
  }

}

object Header {

  implicit val byteOrder = ByteOrder BIG_ENDIAN

  def parse(iter: ByteIterator): Header = {
    val ID = iter getShort
    val flags = Flags(iter getShort)
    val QDCOUNT = iter getShort
    val ANCOUNT = iter getShort
    val NSCOUNT = iter getShort
    val ARCOUNT = iter getShort

    Header(ID, flags, QDCOUNT, ANCOUNT, NSCOUNT, ARCOUNT)
  }

}
