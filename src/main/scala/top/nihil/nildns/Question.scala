package top.nihil.nildns

import akka.util.{ByteIterator, ByteString, ByteStringBuilder}

case class Question(QNAME:String, QTYPE:RecordType.Value, QCLASS:RecordClass.Value) {
  def write(out:ByteStringBuilder): Unit ={
    DomainName write(out, QNAME)
    RecordType.write(out,QTYPE)
    RecordClass.write(out,QCLASS)
  }
}

object Question{
  def parse(iter:ByteIterator,msg:ByteString):Question={
    val QNAME = DomainName.parse(iter, msg)
    val QTYPE = RecordType.parse(iter)
    val QCLASS = RecordClass.parse(iter)
    Question(QNAME,QTYPE,QCLASS)
  }
}
