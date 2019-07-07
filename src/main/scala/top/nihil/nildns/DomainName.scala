package top.nihil.nildns

import akka.util.{ByteIterator,ByteString, ByteStringBuilder}



object DomainName{
  def length(name: String): Short = (name.length + 2).toShort

  def write(it:ByteStringBuilder, name:String): Unit ={
    name.split('.')
      .foreach(label => {
        it putByte label.length.toByte
        label foreach (c => it.putByte(c.toByte))
      })
    it putByte 0
  }

  def parse(it:)

}

