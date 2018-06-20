package com.shujutang.highway.dataimport.clean

import com.shujutang.highway.common.util.RegularUtil
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.time.DateUtils
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 清洗原始数据
  *
  * 1 对车牌号进行识别,并添加标识:正确车牌：1，错误车牌：0,
  *
  * 2 超载：1 未超载：0
  *
  * 3 超速：1 未超速：0
  *
  * 执行:spark-submit --master spark://master:7077 --executor-memory 5g --driver-cores 2 --class com.shujutang.highway.dataimport.clean.CleanSourceData  highway-dataimport-clean.jar
  * Created by nancr on 16/1/28.
  */
@deprecated
class CleanSourceData extends java.io.Serializable{
  private var datePattern: Array[String] = Array("yyyy-MM-dd HH:mm:ss")
  private val reg = "(\\w*[\\u4E00-\\u9FA5]{1,2}\\w{6})" + "|(\\w*?[\\u4E00-\\u9FA5]{1}\\w{5}[\\u4E00-\\u9FA5]{1})" + "|(\\w*?\\p{Punct}[\\u4E00-\\u9FA5]{1,2}\\p{Punct}\\w{5}[\\u4E00-\\u9FA5]{1})" + "|(\\w*?\\p{Punct}[\\u4E00-\\u9FA5]{1,2}\\p{Punct}\\w{6})";
  //需要处理的文件目录。文件名称：2015_hebei_01_01.txt
  val rootPath = "file:/home/spark/file/hiveexprt/"

  /**
    * 清洗源数据
    *
    * @param sc
    * @param cleanFilePath 需要清洗的目录路径
    */
  def clean(sc: SparkContext, cleanFilePath: String): Unit = {
    sc.textFile(cleanFilePath).map(line => {
      var res = "";
      val sbf = new StringBuilder
      val words = line.split(",")
      // 车牌是否有效
      val carlicense = words(9)
      val isMatch = RegularUtil.`match`(carlicense, reg)
      sbf.append(",").append(if (isMatch) 1 else 0)

      //是否超速
      val entime = words(3)
      val exittime = words(7)
      val mileage = words(12)
      val overspeed: Boolean = isOverSpeed(entime, exittime, mileage)
      sbf.append(",").append(if (overspeed == true) 1 else 0)

      //是否超载
      val weight = words(17)
      val limitweight = words(18)
      val overload = isOverLoad(weight, limitweight)
      sbf.append(",").append(if (overload) 1 else 0)

      res = line + sbf.toString()
      res
    }).saveAsTextFile("/user/hadoop/spark/clean/out5")
  }

  /**
    * 是否超载
    *
    * @param weight 车货重量
    * @param limitweight 限重
    */
  def isOverLoad(weight: String, limitweight: String): Boolean ={
    if (StringUtils.isEmpty(weight) || StringUtils.isEmpty(limitweight))
      return false
    return weight.toDouble > limitweight.toDouble
  }

  /**
    * 计算是否超速
    *
    * @param entime 入站时间
    * @param exittime 出站时间
    * @param mileageStr 里程
    * @return
    */
  def isOverSpeed(entime: String, exittime: String, mileageStr: String): Boolean = {
    var mileage: Double = 0
    var maxMileage: Double = 0
    try {
      val startTime: String = entime.substring(0, 19)
      val endTime: String = exittime.substring(0, 19)
      if (mileageStr == null) return false
      mileage = mileageStr.toDouble
      val hours: Double = (DateUtils.parseDate(endTime, datePattern).getTime - DateUtils.parseDate(startTime, datePattern).getTime).toDouble / 3600000
      maxMileage = hours * 120
    } catch {
      case e: Exception => {
        e.printStackTrace()
      }
    }
    return maxMileage < mileage
  }
}

@deprecated
object CleanSourceData{

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("cleanApplication")
    val sc = new SparkContext(conf)

    val cleanSourceData = new CleanSourceData;

    val filePath = "/user/hadoop/spark/clean/highwaydemo";
    cleanSourceData.clean(sc, filePath)
    sc.stop();
  }
}
