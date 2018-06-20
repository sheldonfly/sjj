package com.shujutang.highway.dataimport.imitatedata

import java.util.{Date, Random}
import java.util.concurrent.TimeUnit

import org.apache.commons.lang.time.DateFormatUtils
import org.apache.spark.{SparkContext, SparkConf}

/**
  * ###模拟数据###
  * highway模拟数据导入:
  * 模拟河北数据导入12个省:
  * 数据分区:年/省/月/日
  *
  * 设计: 现在有1月1号的数据.将1号数据读取出来,处理掉日期,插入其他目录
  *
  * 数据样例:
  * 000000000001301,000000000230005,赵县,2014-12-31 23:09:17,000000000001301,000000000230007,新河,
  * 2015-01-01 00:00:00,00102,津HW2000,13,1,48.9,,5112;3601;15352;29509;0;,16219.999999999998,4.0,53574.0,48000.0,11.6,0,
  * 11,,0,d,0,01,1301042,1305011,0

  * 执行: spark-submit --master spark://master:7077 --executor-memory 5g --driver-cores 2 --class com.shujutang.highway.dataimport.imitatedata.HighwayImitateData highway-dataimport-1.0.jar
  * Created by nancr on 16/2/2.
  */
class HighwayImitateData {

}

/**
  * 启动导入服务
  */
object HighwayImitateData{
  val year = "2015";
  val province = "hebei"

  //原始数据地址
  val sourceFilePath = "/user/hive/warehouse/highway/year=2015/province=hebei/month=02/day=01"

  //数据存储根目录
  val rootPath = "/user/hive/warehouse/highway/year=" + year + "/province=" + province + "/";

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("highway_dataimport")
    val sc = new SparkContext(conf)

   imitate(sc)

    sc.stop()
  }

  /**
    * 将12个省的数据处理完成,放到指定目录下
    *
    * @param sc
    */
  def imitate(sc: SparkContext): Unit ={
    val lines = sc.textFile(sourceFilePath)
    lines.cache()

    //循环12个月
    for (month <- 1 to 12){
      val monthStr = new StringBuilder

      if (month < 10) {monthStr.append("0").append(month)} else {monthStr.append(month)}

      val days = getMonthDay(monthStr.toString())

      //处理每天的数据
      for (day <- 1 to days){
          var  dayStr :String = "d";
          if (day < 10) dayStr = "0" + day else dayStr = day + ""

          println("开始执行: 月:" + monthStr +"  日:" + dayStr + " 当前时间:" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"))

          lines.map(line => {
            //处理每行数据,更改日志字段
            val newLine = new StringBuilder
            val lineArr = line.split(",")
            //2015-01-01 00:00:00
            val entime = "2015-" + monthStr + "-" + dayStr + " " + randomInt(12, 0) + ":" + randomInt(59, 0) + ":" +
              randomInt(59, 0)
            val exittime = "2015-" + monthStr + "-" + dayStr + " " + randomInt(24, 13) + ":" + randomInt(59, 0) + ":" +
              randomInt(59, 0)

            for (index <- 0 to lineArr.length - 1){
              if (index == 3) newLine.append(entime).append(",") else if (index == 7) newLine.append(exittime).append(",")
              else if (index == lineArr.length - 1) newLine.append(lineArr(index))
              else newLine.append(lineArr(index)).append(",")
            }

            newLine.toString()

          }).saveAsTextFile(rootPath + "month=" +  monthStr + "/day=" + dayStr + "/")
          //.repartition(1)

          println("执行结束: 月:" + monthStr +"  日:" + dayStr + " 当前时间:" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"))
          TimeUnit.SECONDS.sleep(5)
      }
    }
    lines.unpersist(true)
  }

  /**
    * 月份对应的天数
    */
  def getMonthDay(month: String) : Int = {

    val monthDays = Map[String, Int]("01" -> 31, "03" -> 31, "05" -> 31, "07" -> 31, "08" -> 31, "10" -> 31, "12" -> 31,
      "02" -> 30, "04" -> 30, "06" -> 30, "09" -> 30, "11" -> 30, "02" -> 28)

    return monthDays.get(month).iterator.next()
  }

  /**
    * 随机返回一个int值
    *
    * @param max 最大区间
    * @param min 最小区间
    * @return
    */
  def randomInt(max : Int, min : Int) : Int = {
    val random = new Random();
    val s = random.nextInt(max) % (max - min + 1) + min;
    return s;
  }

}
