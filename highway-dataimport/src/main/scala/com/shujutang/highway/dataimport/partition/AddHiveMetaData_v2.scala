package com.shujutang.highway.dataimport.partition

import java.io.FileWriter
import java.util.Date

import org.apache.commons.lang.time.DateFormatUtils

/**
  * 创建分区表后，生成hive分区命令
  *
  * 指定年和省，将分区指令输出到文件
  *
  * 执行：
  * Created by nancr on 2016/2/3.
  */
@deprecated
object AddHiveMetaData_v2 {
  val year = "2015";
  val province = "hebei"
  //数据存储根目录
  val rootPath = "/user/hive/warehouse/highway/year=" + year + "/province=" + province;

  //cmd输出目录
  val cmdFile = "D:/tmp/hivepartition.txt"

  def main(args: Array[String]) {

    println("开始执行时间:" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"))
    val fileWriter = new FileWriter(cmdFile, true)

    for (month <- 1 to 12){
      var monthStr = ""
      if (month < 10) monthStr = "0" + month else monthStr = month + ""

      val days = getMonthDay(monthStr)

      for (day <- 1 to days){
        try {
          var dayStr: String = "d";
          if (day < 10) dayStr = "0" + day else dayStr = day + ""

         //分区路径
         val cmd = new StringBuilder("\"alter table highway add partition (year='2015', province='hebei',").
           append("month='").append(monthStr).append("',").append("day='").append(dayStr).append("')").
           append(" location ").append("'" + rootPath).append("/month=").append(monthStr).
           append("/day=").append(dayStr).append("';\"").append("\n")
          println("cmd=" + cmd.toString())

          //将信息输出到文件
          fileWriter.write(cmd.toString())

       }catch {
         case e: Exception => e.printStackTrace()
       }
      }
    }

    fileWriter.close()
    println("执行结束时间:" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"))
  }

  /**
    * 月份对应的天数
    */
  def getMonthDay(month: String) : Int = {

    val monthDays = Map[String, Int]("01" -> 31, "03" -> 31, "05" -> 31, "07" -> 31, "08" -> 31, "10" -> 31, "12" -> 31,
      "02" -> 30, "04" -> 30, "06" -> 30, "09" -> 30, "11" -> 30, "02" -> 28)

    return monthDays.get(month).iterator.next()
  }
}
