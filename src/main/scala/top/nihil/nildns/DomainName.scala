package top.nihil.nildns

import akka.util.{ByteIterator, ByteString, ByteStringBuilder}

object DomainName {

  def length(name: String): Short = (name.length + 2).toShort

  def write(iter: ByteStringBuilder, name: String): Unit = {
    name.split('.') foreach (label => {
      iter putByte label.length.toByte
      label foreach (c => iter putByte c.toByte)
    })
    iter putByte 0
  }

  def parse(iter: ByteIterator, msg: ByteString): String = {
    val res = StringBuilder.newBuilder
    while (true) {
      val length = iter.getByte
      if (length == 0) return res.result()
      if (res.nonEmpty) res append '.'
      if ((length & 0xc0) == 0xc0) {
        val offset = ((length.toShort & 0x3f) << 8) | (iter.getByte.toShort * 0xff)
        return res.result() + parse(msg.iterator.drop(offset), msg)
      }
      res appendAll iter.clone().take(length).map(_.toChar)
      iter drop length
    }
    ???
  }

}

