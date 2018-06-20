package com.shujutang.highway.common.util

import java.text.SimpleDateFormat
import java.util
import java.util.{Calendar, Date}

import org.apache.commons.lang.time.{DateFormatUtils, DateUtils}

import scala.collection.mutable

/**
  * 日期生成工具
  *
  * Created by nancr on 2016/2/5.
  */
object DateGenerate extends Serializable{
  val pattern = "yyyyMM"
  val simpleDateFormat = new SimpleDateFormat("yyyyMMdd")
  val simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd-HH:mm:ss")

  /**
    * 得到最近一年的年月
    *
    * @return
    */
 def  getStartAndEndTimeRecent12Months(): String ={
   val endDate = DateUtils.addMonths(new Date(), -1)
   val startDate = DateUtils.addMonths(new Date(), -12)

   val startTime = DateFormatUtils.format(startDate, pattern)
   val endTime = DateFormatUtils.format(endDate, pattern)

    startTime + "," + endTime
 }

  /**
    * 通过开始结束时间，得到所有 年月 列表
    * 开始结束时间可以相同
    *
    * @param startTime 开始时间 如：201501
    * @param endTime 结束时间 如：201501
    * @param pattern 时间格式
    * @return 年月对象的列表 List[OneMonth]
    */
  def getAllMonth(startTime: String, endTime: String, pattern: String = "yyyyMM"): util.ArrayList[OneMonth] ={
    var res = new util.ArrayList[OneMonth]()
    val patternArr = Array(pattern)
    val startDate = DateUtils.parseDate(startTime, patternArr)
    val endDate = DateUtils.parseDate(endTime, patternArr)

    val endLongTime = endDate.getTime
    var nextDate = startDate

    var loop = true

    if (startDate.getTime > endLongTime){
      return res
    }else{
      while(loop){
        val calendar = Calendar.getInstance()
        calendar.setTime(nextDate)
        val year = calendar.get(Calendar.YEAR).toString
        val month = (calendar.get(Calendar.MONTH) + 1)
        var monthStr = month.toString
        if (month < 10) monthStr = "0" + month
        val oneMonth = new OneMonth(year, monthStr)

        res.add(oneMonth)

        nextDate = DateUtils.addMonths(nextDate, 1)
        if (nextDate.getTime > endLongTime)
          loop = false
      }
    }
    res
  }

  /**
    * 根据开始结束时间 生成对象列表
    * List<OneTime>
    *
    * @param startTime 如：20150101
    * @param endTime 如：20150101
    * @param province 省份
    * @param pattern 时间格式
    * @return
    */
  def getAllDay(startTime: String, endTime: String, province: String, pattern: String): util.ArrayList[OneTime] ={
    val patternArr = new Array[String](1)
    patternArr(0) = pattern
    val startDate = DateUtils.parseDate(startTime, patternArr)
    val endDate = DateUtils.parseDate(endTime, patternArr)

    //查询条件列表
    var list = new util.ArrayList[OneTime]()

    val startTimeObj = handleDate(startDate, province)
    list.add(startTimeObj)

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

    val endTimeObj = handleDate(endDate, province)
    list.add(endTimeObj)

    //删除开始结束时间为同一天的情况
    if (startTimeObj.y.equals(endTimeObj.y) && startTimeObj.m.equals(endTimeObj.m) && startTimeObj.d.equals(endTimeObj.d)){
      list.remove(endTimeObj)
    }

    list
  }

  /**
    * 得到当前时间的字符串
    * @return
    */
  def getCurrentDate(): String ={
    return simpleDateFormat.format(new Date)
  }
  def getCurrentDate2(): String ={
    return simpleDateFormat2.format(new Date)
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

    var monthStr = month.toString
    var dayStr = day.toString
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

  /**
    * 得到指定开始结束时间段中的季度
    *
    * @param startTime 201501
    * @param endTime 201601
    */
  def getAllQuarTers(startTime: String, endTime: String): mutable.HashMap[String, OneQuarter] ={
    val allMonths = getAllMonth(startTime, endTime)
    val  it = allMonths.iterator()

    //所有季度，以及季度对应的开始结束月份
    val allQuarters = new mutable.HashMap[String, OneQuarter]()

    while (it.hasNext){
      val curTime = it.next()
      val year = curTime.y
      val month = curTime.m
      getCurrentQuarter(year, month, allQuarters)
    }
    allQuarters
  }

  /**
    * 通过月份得到当前属于哪个季度
    *
    * @param year 年
    *
    * @param month 月
    */
  def getCurrentQuarter(year: String, month: String, map: mutable.HashMap[String, OneQuarter]): Unit ={
    //当前月所属季度
    var curQuarter = ""

    def addQuarterForFirstMonth: Unit = {
      val oneQuarter = new OneQuarter
      oneQuarter.setFirstMonth(s"$year$month")
      oneQuarter.setThirdMonth(s"$year$month")
      map.put(curQuarter, oneQuarter)
    }

    def addQuarterForSecondMonth: Unit = {
      val oneQuarter = map.get(curQuarter)
      if (oneQuarter == None) {
        addQuarterForFirstMonth
      }else{
        oneQuarter.get.setThirdMonth(s"$year$month")
      }
    }
    def addQuarterForThirdMonth: Unit = {
      val oneQuarter = map.get(curQuarter)
      if (oneQuarter == None) {
        addQuarterForFirstMonth
      } else {
        oneQuarter.get.setThirdMonth(s"$year$month")
      }
    }

    month match {
      case "01" => {
        curQuarter = s"${year}01"
        addQuarterForFirstMonth
      }
      case "02" => {
        curQuarter = s"${year}01"
        addQuarterForSecondMonth
      }
      case "03" => {
        curQuarter = s"${year}01"
        addQuarterForThirdMonth
      }
      case "04" => {
        curQuarter = s"${year}02"
        addQuarterForFirstMonth
      }
      case "05" => {
        curQuarter = s"${year}02"
        addQuarterForSecondMonth
      }
      case "06" => {
        curQuarter = s"${year}02"
        addQuarterForThirdMonth
      }
      case "07" => {
        curQuarter = s"${year}03"
        addQuarterForFirstMonth
      }
      case "08" => {
        curQuarter = s"${year}03"
        addQuarterForSecondMonth
      }
      case "09" => {
        curQuarter = s"${year}03"
        addQuarterForThirdMonth
      }
      case "10" => {
        curQuarter = s"${year}04"
        addQuarterForFirstMonth
      }
      case "11" => {
        curQuarter = s"${year}04"
        addQuarterForSecondMonth
      }
      case "12" => {
        curQuarter = s"${year}04"
        addQuarterForThirdMonth
      }
      case _ =>
    }
//    curQuarter
  }

}

class OneTime(year : String, province: String, month : String, day:String) {
  val y = year
  val p = province
  val m = month
  val d = day
}

class OneMonth(year: String, month: String){
  val y = year
  val m = month
}

/**
  * 一个季度
  */
class OneQuarter{
  //第一个月
  var firstMonth = ""
//  //第二个月
//  var secondMonth = ""
  //第三个月
  var thirdMonth = ""

  def setFirstMonth(first: String): Unit ={
    firstMonth = first
  }

  def setThirdMonth (third : String): Unit ={
    thirdMonth = third
  }
}