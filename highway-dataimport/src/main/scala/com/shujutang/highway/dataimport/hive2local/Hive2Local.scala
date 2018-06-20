package com.shujutang.highway.dataimport.hive2local

import java.sql.DriverManager
import java.util
import java.util.{Date, Calendar}

import com.shujutang.highway.common.util.{OneTime, JdbcUtil}
import org.apache.commons.lang.time.DateUtils

/**
  * hive数据倒出到本地文件
  *
  * Created by nancr on 2016/2/4.
  */
@deprecated
object Hive2Local {
  //连接hive
  val driverName = "org.apache.hive.jdbc.HiveDriver"
  val url = "jdbc:hive2://192.168.3.72:10000/default"

  //开始和结束时间(包括开始结束时间)
  val startTime = "20150101"
  val endTime = "20150105"

  val pattern = "yyyyMMdd"

  def main(args: Array[String]) {
    //hive连接
    try {
      Class.forName(driverName);
    } catch  {
      case e: Exception => e.printStackTrace()
        System.exit(1);
    }
    //replace "hive" here with the name of the user the queries should run as
    val connection = DriverManager.getConnection(url, "spark", "");

    if (connection == null) {println("#####hive连接为空");return} else {"######hive连接不为空：" + connection.toString;}

//    val sql = new StringBuilder
//    sql.append("insert overwrite  local directory '/home/spark/file/hiveexprt/a.txt' " +
//      "select * from highway where year='2015' and province='hebei' and month='03' and day='01';")

    //通过设定时间获取每天时间对象
    if (!checkTime(startTime, endTime))
      throw new RuntimeException("开始时间必须小于结束时间")

    //根据开始结束时间创建查询条件对象
    val argsList = queryObj(startTime, endTime, "hebei", pattern)

    val it = argsList.iterator()
    while (it.hasNext){
      val onetime = it.next();
      val sql = new StringBuilder("insert overwrite  local directory '/home/spark/file/hiveexprt/")
      sql.append(onetime.y).append("_").append(onetime.p).append("_").append(onetime.m).append("_").append(onetime.d).append(".txt' ").
        append("select * from highway where year=? and province=? and month=? and day=?")

      val res = JdbcUtil.insertOrUpdateDB(connection, sql.toString(), onetime.y, onetime.p, onetime.m, onetime.d)
      println("导出结果：" + res)
    }

  }

  def queryObj(startTime: String, endTime: String, province: String, pattern: String): util.ArrayList[OneTime] ={
    val patternArr = new Array[String](1)
    patternArr(0) = pattern
    val startDate = DateUtils.parseDate(startTime, patternArr)
    val endDate = DateUtils.parseDate(endTime, patternArr)

    //查询条件列表
    var list = new util.ArrayList[OneTime]()

    val timeObjStart = handleDate(startDate, province)
    list.add(timeObjStart)

    var preDate = startDate
    var loop = true
    while (loop){
      val nextDay = DateUtils.addDays(preDate, 1)
      if (nextDay.getTime < endDate.getTime)
        list.add(handleDate(nextDay, province))
      else
        loop = false

      preDate = nextDay
    }

    list.add(handleDate(endDate, province))
    list
  }

  /**
    * 将时间处理成 年月日格式
    *
    * @param date 时间
    * @return
    */
  def handleDate(date: java.util.Date, province: String): OneTime ={
    val cal = Calendar.getInstance()
    cal.setTime(date)
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH) + 1
    val day = cal.get(Calendar.DAY_OF_MONTH)

    var monthStr = "" + month
    var dayStr = "" + day
    if (month < 10) monthStr = "0" + month
    if (day < 10) dayStr = "0" + day

    val oneTime = new OneTime(year + "", province, monthStr, dayStr)
    oneTime
  }

  /**
    * 判断开始结束时间是否正确
    *
    * 格式：yyyyMMdd
    * 开始时间《结束时间
    *
    * @param startTime
    * @param endTime
    * @return
    */
  def checkTime(startTime: String, endTime: String): Boolean ={
    val pattern = new Array[String](1)
    pattern(0) = "yyyyMMdd"

    var res: Boolean = true

    try {
      val startDate = DateUtils.parseDate(startTime, pattern)
      val endDate = DateUtils.parseDate(endTime, pattern)
      if (startDate.getTime > endDate.getTime) res = false
    } catch {
      case e: Exception => {e.printStackTrace(); println("时间格式不正确！！！")}
    }

    res
  }
}

//class TimeObj(year: String, month: String, day: String){
//  val y = year
//  val m = month
//  val d = day
//}



