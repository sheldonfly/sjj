package samples

import com.shujutang.highway.common.util.DateGenerate

/**
  * Created by nancr on 2016/2/18.
  */
class DateTest {


}

object DateTest{
  def main(args: Array[String]) {
    val startTime = "201501"
    val endTime = "201501"
    val allmonths  = DateGenerate.getAllMonth(startTime, endTime, "yyyyMM")
    val  it = allmonths.iterator
    while (it.hasNext){
      val oneMonth = it.next()
      println(oneMonth.y + "  " + oneMonth.m )
    }
  }
}
