package com.shujutang.highway.dataimport.check

import java.io.FileWriter

import com.shujutang.highway.common.domain.Province
import com.shujutang.highway.common.util.{ContextUtil, DateGenerate}
import org.apache.log4j.{Level, Logger}

/**
  * 检查原始数据哪些天没有数据
  *
  * spark-submit --master spark://hadoop160:7077  --executor-memory 3g --total-executor-cores 10
  * --class com.shujutang.highway.dataimport.check.SourceDataBaseCheck_isExit /data/home/spark/file/jar/highway-dataimport-check.jar
  *
  *
  * Created by nancr on 2016/5/20.
  */
object SourceDataBaseCheck_isExit extends ContextUtil{
  	Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
  	Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
  val startTime = "20160101"
  val endTime = "20160331"
  val pattern = "yyyyMMdd"
  val filePath = "hdfs://192.168.203.161:8020/user/hadoop/highway/"

  def main(args: Array[String]) {
    //报告输出路径
    val fw = new FileWriter("wanzhengxing.txt", true)

    //循环所有省数据
    for (province <- Province.haveDataProvinc.keySet) {
      val allDays = DateGenerate.getAllDay(startTime, endTime, province, pattern)
      val it = allDays.iterator()
      while (it.hasNext){
        val oneday = it.next()
        val year = oneday.y
        val month = oneday.m
        val day = oneday.d

        println
        println("########################处理：[" + province + "  " + year + "  " + month + "] 当前时间：" + DateGenerate.getCurrentDate2 +
          "######################")

        val fileName = year + "_" + province + "_" + month + "_" + day + ".txt"
        try {
          val count = sc.textFile(s"$filePath/$year/$province/$fileName").count()
          if (count <= 2){
            fw.write("文件：" + fileName + "  无数据\n")
          }else{
            println("#############总记录数=" + count)
          }


        } catch {
          case e: Exception => {
            e.printStackTrace()
            fw.write("文件：" + fileName + "  不存在\n")
          }
        }
        fw.flush()
        println("##################### [\" + province + \"  \" + year + \"  \" + month + \"]完成  " +
          "当前时间：" + DateGenerate.getCurrentDate2 + "######################")
      }
    }

    fw.close()
    sc.stop()
  }
}
