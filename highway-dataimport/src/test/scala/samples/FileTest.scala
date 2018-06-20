package samples

import java.io.PrintWriter

import com.shujutang.highway.common.util.RegularUtil

import scala.io.Source

/**
  * Created by nancr on 2016/3/18.
  */
object FileTest {
  def main(args: Array[String]) {
    var source: Source = null
    var pw: PrintWriter = null
    try {
      val res = new StringBuilder
      source = Source.fromInputStream(FileTest.getClass.getClassLoader.getResourceAsStream("file/dim_shoufeizhan_tmp.txt"))
      source.getLines().foreach(line => {
        val arr = line.split("\t")
        val sfzmc = handleStr(arr(7))
        val cityname = handleStr(arr(8))
        arr(7) = sfzmc
        arr(8) = cityname

        val linestr = new StringBuilder
        arr.foreach(word => {
          linestr.append(word).append(",")
        })
        val line2 = linestr.toString
        res.append(line2.substring(0, line2.length - 1)).append("\n")
      })

      pw = new PrintWriter("D:/tmp/dim_shoufeizhan_tmp2.txt")
      pw.print(res.toString())
    } catch {
      case e : Exception =>{
        e.printStackTrace()
      }
    }finally {
      source.close
      pw.close
    }
  }

  import util.control.Breaks._

  def handleStr(str: String): String = {

    val reg = "^[\\u4e00-\\u9fa5]"
    var index = 0

    breakable {
      for (char <- str) {
        val ismatch = RegularUtil.`match`(char.toString, reg)
        if (ismatch)
          break;
        index += 1
      }
    }

    val res = str.substring(index, str.length)
    res
  }


}
