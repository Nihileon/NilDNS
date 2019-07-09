package top.nihil.nildns

case class FileParser(filePath: String) {
}

object FileParser {
  def parse(filePath: String): Map[String, String] = {
    val lineArray = scala.io.Source.fromFile(filePath).getLines.toArray
    lineArray.map(s => {
      val p = s.split("\\.")
      (p(1), p(0))
    }).toMap
  }
}
