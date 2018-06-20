package com.shujutang.highway.dataimport.check

import java.io.{FileWriter, PrintWriter}
import java.text.NumberFormat

import com.shujutang.highway.common.domain.Province
import com.shujutang.highway.common.util.{ContextUtil, DateGenerate, RegularUtil, ShoufeizhanUtil}
import org.apache.log4j.{Level, Logger}

/**
  * 检查原始数据-是否正确
  * 每个月统计：（1）统计记录总数，cache（2）统计每一项的数量，过滤后，输出结果。每一项都单独处理
  * 1、没有出口时间 （“”或者NULL）
  * 2、车牌错误的比例 （正则）
  * 3、车入口时间不一样，里程为0 （出入口时间不vhb同，但是里程为0）
  * 4、出入口在字典表中查不到 （根据收费站编号，获取不到名称）
  * 5、每个月的数据量 （统计各个月的数量）
  * 6、车货总重为零的比例（当前月）
  *
  * spark-submit --master spark://hadoop160:7077  --executor-memory 3g --total-executor-cores 10
  * --class com.shujutang.highway.dataimport.check.SourceDataBaseCheck_data /data/home/spark/file/jar/highway-dataimport-check.jar
  *
  *
  * Created by nancr on 2016/5/20.
  */
object SourceDataBaseCheck_data extends ContextUtil {
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
  Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
  val startTime = "201601"
  val endTime = "201603"
  val pattern = "yyyyMM"
  val filePath = "hdfs://192.168.203.161:8020/user/hadoop/highway/"

  def main(args: Array[String]) {
    //报告输出路径
    val fw_all = new PrintWriter(new FileWriter("check_data_2016.txt"), true)
//    val fw_error = new PrintWriter(new FileWriter("check_data_error.txt"), true)

    val numberFormat = NumberFormat.getInstance();
    numberFormat.setMaximumFractionDigits(4);
    val broadcastNumberFormat = sc.broadcast(numberFormat)

    //循环所有省数据
    for (province <- Province.haveDataProvinc.keySet) {
      val allMonth = DateGenerate.getAllMonth(startTime, endTime, pattern)
      val it = allMonth.iterator()
      while (it.hasNext) {
        val oneMonth = it.next()
        val year = oneMonth.y
        val month = oneMonth.m

        println
        println("########################处理：[" + province + "  " + year + "  " + month + "] 当前时间：" + DateGenerate.getCurrentDate2 +
          "######################")

        val fileName = year + "_" + province + "_" + month + "*"
        try {
          val file = sc.textFile(s"$filePath/$year/$province/$fileName")

          //当前月总记录
          val totalCount = file.count()
          val bvTotalCount = sc.broadcast(totalCount)

          val outtimeAccumulator = sc.accumulator(0, "outtime")
          val carlicenseAccumulator = sc.accumulator(0, "carlicense")
          val mileageAccumulator = sc.accumulator(0, "mileage")
          val outStationNumAccumulator = sc.accumulator(0, "outStationNum")
          val enterStarionNumAccumulator = sc.accumulator(0, "enterStarionNum")
          val chzzAccumulator = sc.accumulator(0, "chzz")

          val res = file.map(line => {
            var res: String = ""
            //1、没有出口时间 （“”或者NULL）
            val words = line.split("\t")
            if (words.length == 23 && !(words.contains("chzz"))) {
              val outTime = words(5).trim
              if (!RegularUtil.`match`(outTime, Params.date) && outTime.length != 19) {
                outtimeAccumulator += 1
              }

              //2、车牌错误的比例 （正则）
              val carlicense = words(6).trim
              val isMatch = RegularUtil.`match`(carlicense, Params.carlicenseReg)
              if (!isMatch) {
                carlicenseAccumulator += 1
                res = carlicense
              }

              //3、车入口时间不一样，里程为0 （出入口时间不同，但是里程为0）
              val enterTime = words(2).trim
              var mileage = 0D
              try {
                mileage = words(9).toDouble
              } catch {
                case e: Exception =>
              }
              if (!enterTime.equals(outTime) && mileage == 0D){
                mileageAccumulator += 1
              }

              //4、出入口在字典表中查不到 （根据收费站编号，获取不到名称）
              val rkwlbh = words(0).trim
              val rkzbh = words(1).trim

              val ckwlbh = words(3).trim
              val ckzbh = words(4).trim

              val enterStarionNum = s"$province$rkwlbh$rkzbh"
              val outStationNum = s"$province$ckwlbh$ckzbh"

              if (ShoufeizhanUtil.getStationName(enterStarionNum).equals("none")){
                enterStarionNumAccumulator += 1
              }

              if (ShoufeizhanUtil.getStationName(outStationNum).equals("none")){
                outStationNumAccumulator += 1
              }

              //5、车货总重为零的比例
              var chzz = 0D
              try {
                chzz = words(12).toDouble
              } catch {
                case e: Exception =>
              }
              if (chzz == 0){
                chzzAccumulator += 1
              }

            }
            res
          }).count()
//            .sample(true, 2D).collect().mkString("\t")


          val outTimeErrorProportion = broadcastNumberFormat.value.format(outtimeAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"
          val carlicenseErrorProportion = broadcastNumberFormat.value.format(carlicenseAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"
          val mileageErrorProportion = broadcastNumberFormat.value.format(mileageAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"
          val enterStarionNumErrorProportion = broadcastNumberFormat.value.format(enterStarionNumAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"
          val outStationNumErrorProportion = broadcastNumberFormat.value.format(outStationNumAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"
          val chzzErrorProportion = broadcastNumberFormat.value.format(chzzAccumulator.value.toFloat / totalCount.toFloat * 100) + "%"

          fw_all.println(s"[$province  $year-$month] 出站时间错误比例：$outTimeErrorProportion    错误次数：${outtimeAccumulator.value}")
          fw_all.println(s"[$province  $year-$month] 车牌错误比例：$carlicenseErrorProportion     错误次数：${carlicenseAccumulator.value}")
          fw_all.println(s"[$province  $year-$month] 里程=0比例：$mileageErrorProportion     错误次数：${mileageAccumulator.value}")
          fw_all.println(s"[$province  $year-$month] 进站口编号错误比例：$enterStarionNumErrorProportion     错误次数：${enterStarionNumAccumulator.value}")
          fw_all.println(s"[$province  $year-$month] 出站口编号错误比例：$outStationNumErrorProportion     错误次数：${outStationNumAccumulator.value}")
          fw_all.println(s"[$province  $year-$month] 车货总重=0比例：$chzzErrorProportion     错误次数：${chzzAccumulator.value}")
          fw_all.println(s"[$province  $year-$month] 当前省、月总数据量：$totalCount ")
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
