package com.shujutang.highway.dataimport.partition

import java.sql.DriverManager
import java.util.Date

import com.shujutang.highway.common.util.JdbcUtil
import org.apache.commons.lang.time.DateFormatUtils

/**
  * 将分区信息插入hive metastore中
  *
  * 执行：
  *  spark-submit --master spark://master:7077 --executor-memory 1g --driver-cores 2 --class com.shujutang.highway.dataimport.imitatedata.AddHiveMetaData highway-dataimport-1.0.jar
  * Created by nancr on 2016/2/3.
  */
@deprecated
object AddHiveMetaData {
  val year = "2015";
  val province = "hebei"
  //数据存储根目录
  val rootPath = "/user/hive/warehouse/highway/year=" + year + "/province=" + province + "/";

  //连接hive
  val driverName = "org.apache.hive.jdbc.HiveDriver"
  val url = "jdbc:hive2://master:10000"

  def main(args: Array[String]) {
    //hive连接
    try {
      Class.forName(driverName);
    } catch  {
      case e: Exception => e.printStackTrace()
      System.exit(1);
    }
    //replace "hive" here with the name of the user the queries should run as
   val connection = DriverManager.getConnection("jdbc:hive2://192.168.3.72:10000/default", "spark", "");

    if (connection == null) {println("#####hive连接为空");return} else {"######hive连接不为空：" + connection.toString;}
    val sql = new StringBuilder
    sql.append("alter table highway add partition (year='2015', province='hebei', month=?, day=?) location \"")


    println("开始执行时间:" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"))
    for (month <- 1 to 12){
      var monthStr = ""
      if (month < 10) monthStr = "0" + month else monthStr = month + ""

      val days = getMonthDay(monthStr)

      for (day <- 1 to days){
       try {
         var dayStr: String = "d";
         if (day < 10) dayStr = "0" + day else dayStr = day + ""

         //分区路径
         sql.append(rootPath + "month=" + monthStr + "/day=" + dayStr).append("\"")

         JdbcUtil.insertOrUpdateDB(connection, sql.toString(), monthStr, dayStr)
       }catch {
         case e: Exception => e.printStackTrace()
       }
      }
    }
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
