import java.util.Date

import com.shujutang.highway.common.util.{DateGenerate, DateUtil, DateUtilNew}

/**
  * Created by nancr on 2016/7/20.
  */
object DateTest {
  def main(args: Array[String]) {
    val times = DateUtilNew.format(new Date, "yyyyMMdd")
    println(times)
  }

}
