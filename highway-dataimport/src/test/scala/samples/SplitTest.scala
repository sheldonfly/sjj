package samples

import java.util.regex.Pattern

import com.shujutang.highway.common.util.RegularUtil
import org.apache.commons.lang.time.DateUtils

/**
  * Created by wangmeng on 2016/3/23.
  */
class SplitTest {

}

object SplitTest{
  def main(args: Array[String]) {
//    val teststring ="14Shanxi,000000001408103,000000000001401,北留收费站,山西,晋城"
    // var datePattern = Array("yyyy-MM-dd HH:mm:ss")
//    val reg = "(\\w*[\\u4E00-\\u9FA5]{1,2}\\w{6})" + "|(\\w*?[\\u4E00-\\u9FA5]{1}\\w{5}[\\u4E00-\\u9FA5]{1})" + "|(\\w*?\\p{Punct}[\\u4E00-\\u9FA5]{1,2}\\p{Punct}\\w{5}[\\u4E00-\\u9FA5]{1})" + "|(\\w*?\\p{Punct}[\\u4E00-\\u9FA5]{1,2}\\p{Punct}\\w{6})";
//   val carlicense = teststring.split("\t")(3).trim
//    val cph = ""
//    val carlicense = cph.replace("\"","").trim
//    val isMatch = RegularUtil.`match`(carlicense, reg)
//    println(carlicense)
//    println(isMatch)
    val datePattern = Array("yyyy-MM-dd HH:mm:ss")
    val endTime ="2016-02-28 16:29:08"
      val startTime = "2016-02-28 16:28:14"

val hours: Double = (DateUtils.parseDate(endTime, datePattern).getTime - DateUtils.parseDate(startTime, datePattern).getTime).toDouble / 3600000
    val maxMileage = hours * 120
    print(maxMileage)
    //    val eL = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$"
//    val datetime = "2014-01-01 21:31:30"
//    val p = Pattern.compile(eL)
//    val m1 = p.matcher(datetime)
//    val flag1 = m1.matches()
//    if(flag1){
//     println("时间格式匹配正确")
//    }else{
//      println("时间格式不匹配")
//    }

//    val group = teststring.split(",")
//    println(group.length)
//    if(group(0).equals("")){
//      println("第一个字段是空")
//    }
//    for (test <- group){
//      println(test)
//    }

  }
}