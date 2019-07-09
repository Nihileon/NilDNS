package top.nihil.nildns

object OPCODE extends Enumeration {
  val QUERY = Value(0)
  val IQUERY = Value(1)
  val STATUS = Value(2)
}

object RCODE extends Enumeration {
  val NO_ERROR = Value(0)
  val FORMAT_ERROR = Value(1)
  val SERVER_FAILURE = Value(2)
  val NAME_ERROR = Value(3)
  val NOT_IMPLEMENTED = Value(4)
  val REFUSED = Value(5)
}


case class Flags(flags: Short) extends AnyVal {

  def isQuery: Boolean = (flags & (1 << 15)) == 0

  def isAnswer: Boolean = !isQuery

  def opCode: OPCODE.Value = OPCODE((flags & 0x7800) >> 11)

  def isAA: Boolean = (flags & (1 << 10)) != 0

  def isTC: Boolean = (flags & (1 << 9)) != 0

  def isRD: Boolean = (flags & (1 << 8)) != 0

  def isRA: Boolean = (flags & (1 << 7)) != 0

  def rCode: RCODE.Value = RCODE(flags & 0xf)

  def toShort: Short = flags
}

object Flags {

  def get(answer: Boolean = false, opCode: OPCODE.Value = OPCODE QUERY, AA: Boolean = false,
            TC: Boolean = false, RD: Boolean = true, RA: Boolean = false,
            rCode: RCODE.Value = RCODE NO_ERROR): Flags = {

    new Flags((
      (if (answer) 1 << 15 else 0) |
        (opCode.id << 11) |
        (if (AA) 1 << 10 else 0) |
        (if (TC) 1 << 9 else 0) |
        (if (RD) 1 << 8 else 0) |
        (if (RA) 1 << 7 else 0) |
        rCode.id)
      .toShort)
  }

}
