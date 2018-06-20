package com.shujutang.highway.dataimport.check

import java.io.{FileWriter, PrintWriter}
import java.text.NumberFormat

import com.shujutang.highway.common.domain.Province
import com.shujutang.highway.common.util.{DateGenerate, RegularUtil}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by wangmeng on 2016/5/24.
  */
object ResultDataCheck {
  val startTime = "201601"
  val endTime = "201603"
  val pattern = "yyyyMM"
  val filePath = "hdfs://192.168.203.161:8020/user/hive/warehouse/highway/"

  def main(args: Array[String]) {
//    val startTime = args(0)
//    val endTime = args(1)
    val conf = new SparkConf().setAppName("resultdatacheck").setAppName("spark://hadoop160:7077")
    val sc = new SparkContext(conf)
    //报告输出路径
    val fw_all = new PrintWriter(new FileWriter("check_data_all.txt"), true)

    val numberFormat = NumberFormat.getInstance();
    numberFormat.setMaximumFractionDigits(4);
    val broadcastNumberFormat = sc.broadcast(numberFormat)

    val outtimeAccumulator = sc.accumulator(0, "outtime")
    val outtimeformatAccumulator = sc.accumulator(0, "outtimeformat")
    val overspeedAccumulator = sc.accumulator(0, " overspeed")
    val overweightAccumulator = sc.accumulator(0, " overweight")
    val nightoutAccumulator = sc.accumulator(0, " nightout")
    val weekoutAccumulator = sc.accumulator(0, "weekout")
    val festivaloutAccumulator = sc.accumulator(0, "festivalout")
    val tireddriveAccumulator = sc.accumulator(0, "tireddrive")
    val carofproAccumulator = sc.accumulator(0, "carofpro")
    val isProvinceAccumulator = sc.accumulator(0, "isProvince")
    val isValidAccumulator = sc.accumulator(0, "isValid")

    for (province <- Province.haveDataProvinc.keySet) {
      val allMonth = DateGenerate.getAllMonth(startTime, endTime, pattern)
//      val alldays  = DateGenerate.getAllDay(startTime, endTime, province, pattern)
      val it = allMonth.iterator()
      while (it.hasNext) {
        val oneMonth = it.next()
        val year = oneMonth.y
        val month = oneMonth.m
        println
        println("########################处理：[" + province + "  " + year + "  " + month + "] 当前时间：" + DateGenerate.getCurrentDate2 +
          "######################")

        val checkPath = filePath + "year=" + year + "/province=" + province + "/month=" + month + "/day=*/"
        try {
          val file = sc.textFile(checkPath)
          //当月文件总量
          val totalCount = file.count()
          val bvTotalCount = sc.broadcast(totalCount)

          val res = sc.textFile(checkPath).map(line => {
            val words = line.split("\t")
            if (words.length == 40) {

              var carlicense = words(6)

              val isMatch = RegularUtil.`match`(carlicense, Params.carlicenseReg)

              var sortcarlicense: String = null

              try {
                sortcarlicense = carlicense.substring(0, 1)
              } catch {
                case e: Exception => e.printStackTrace();
              }

              val isInMap = Province.provinceMap.contains(sortcarlicense)
              val catofprovince = words(26)

              //统计车牌有效，所属省为none数量
              if (isMatch && isInMap) {
                if (catofprovince.equals("none")) {
                  carofproAccumulator += 1
                }
              }

              //统计车牌有效为本地，计算结果为异地车数量
              val isProvince = words(28)
              if (sortcarlicense.equals(Province.haveDataProvinc.get(province))) {
                if (isProvince.equals("0")) {
                  isProvinceAccumulator += 1
                }
              }

              //统计车牌有效，计算结果却为无效数量
              val isvalid = words(23)
              if (isMatch && isInMap) {
                if (isvalid.equals("1")) {
                  isValidAccumulator += 1
                }
              }

              val outTime = words(5).trim

              //统计出口时间格式不正确的数量

              if (!RegularUtil.`match`(outTime, Params.date)){
                outtimeformatAccumulator += 1
              }

              //统计出口时间为空串或者NULL数

              if (outTime.equals("") || outTime.equals("NULL")) {
                outtimeAccumulator += 1
              }
              //超速数量统计
              val overspeed = words(24)
              if (overspeed.equals("1")) {
                overspeedAccumulator += 1
              }

              //超载数量统计
              val overweight = words(25)
              if (overweight.equals("1")) {
                overweightAccumulator += 1
              }
              //夜间出行数量统计
              val nightout = words(33)
              if (nightout.equals("1")) {
                nightoutAccumulator += 1
              }
              //周末出行数量统计
              val weekout = words(34)
              if (weekout.equals("1")) {
                weekoutAccumulator += 1
              }
              //节假日出行量统计
              val festivalout = words(35)
              if (festivalout.equals("1")) {
                festivaloutAccumulator += 1
              }
              //疲劳驾驶量统计
              val tireddrive = words(36)
              if (tireddrive.equals("1")) {
                tireddriveAccumulator += 1
              }

            }
          }).count()

          val noneofFileProportion = broadcastNumberFormat.value.format(carofproAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"
          val isnotProvinceProportion = broadcastNumberFormat.value.format(isProvinceAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"
          val isnotvalidProportion = broadcastNumberFormat.value.format(isValidAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"
          val outtimeisnullProportion = broadcastNumberFormat.value.format(outtimeAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"
          val outtimeformatProportion = broadcastNumberFormat.value.format(outtimeformatAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"
          val overspeedProportion = broadcastNumberFormat.value.format(overspeedAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"
          val overweightProportion = broadcastNumberFormat.value.format(overweightAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"
          val nightoutProportion = broadcastNumberFormat.value.format(nightoutAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"
          val weekoutProportion = broadcastNumberFormat.value.format(weekoutAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"
          val festivaloutProportion = broadcastNumberFormat.value.format(festivaloutAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"
          val tireddriveProportion = broadcastNumberFormat.value.format(tireddriveAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"

          fw_all.println(s"$province 省 $month 月总条数为：$totalCount")
          fw_all.println(s"[$province  $year-$month]统计车牌有效，所属省为none比例：$noneofFileProportion               错误条数：${carofproAccumulator.value}")
          fw_all.println(s"[$province  $year-$month]统计车牌有效为本地，计算结果为异地车比例：$isnotProvinceProportion     错误条数：${isProvinceAccumulator.value}")
          fw_all.println(s"[$province  $year-$month]统计车牌有效，计算结果却为无效比例：$isnotvalidProportion             错误条数：${isValidAccumulator.value}")
          fw_all.println(s"[$province  $year-$month]统计出口时间为空串或者NULL比例：$outtimeisnullProportion             错误条数：${outtimeAccumulator.value}")
          fw_all.println(s"[$province  $year-$month]统计出口时间格式不正确比例:$outtimeformatProportion                 错误条数：${outtimeformatAccumulator.value}")
          fw_all.println(s"[$province  $year-$month]统计超速比例：$overspeedProportion                                超速条数：${overspeedAccumulator.value}")
          fw_all.println(s"[$province  $year-$month]统计超载比例：$overweightProportion                               超载条数：${overweightAccumulator.value}")
          fw_all.println(s"[$province  $year-$month]统计夜间出行比例：$nightoutProportion                              夜间出行条数：${nightoutAccumulator.value}")
          fw_all.println(s"[$province  $year-$month]统计周末出行比例：$weekoutProportion                               周末出行条数：${weekoutAccumulator.value}")
          fw_all.println(s"[$province  $year-$month]统计节假日出行比例：$festivaloutProportion                         节假日出行条数：${festivaloutAccumulator.value}")
          fw_all.println(s"[$province  $year-$month]统计疲劳驾驶比例：$tireddriveProportion                            疲劳驾驶条数：${tireddriveAccumulator.value}")
          fw_all.println("===============================================================================")
          fw_all.println()

        } catch {
          case e: Exception => {
            e.printStackTrace()
          }
        }

        println("##################### [\" + province + \"  \" + year + \"  \" + month + \"]完成  " +
          "当前时间：" + DateGenerate.getCurrentDate2 + "######################")
        fw_all.flush()
      }
    }
       fw_all.close()
      sc.stop()
    }
  }
