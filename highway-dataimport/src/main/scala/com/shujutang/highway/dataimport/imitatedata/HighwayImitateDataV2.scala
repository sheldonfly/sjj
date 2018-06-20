package com.shujutang.highway.dataimport.imitatedata

import java.io.File
import java.net.URI
import java.util.Random

import com.shujutang.highway.common.domain.Province
import com.shujutang.highway.common.util.{DateGenerate, RegularUtil}
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.time.DateUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 模拟数据
  *
  * nohup spark-submit --master spark://hadoop1:7077 --executor-memory 8g --class com.shujutang.highway.dataimport.imitatedata.HighwayImitateDataV2 highway-dataimport-1.0.jar &
  * Created by nancr on 16/1/28.
  */
class HighwayImitateDataV2 extends java.io.Serializable {
  val startTime = "20150224"
  val endTime = "20151231"
  var datePattern = Array("yyyy-MM-dd HH:mm:ss")
  val pattern = "yyyyMMdd"
  val reg = "(\\w*[\\u4E00-\\u9FA5]{1,2}\\w{6})" + "|(\\w*?[\\u4E00-\\u9FA5]{1}\\w{5}[\\u4E00-\\u9FA5]{1})" + "|(\\w*?\\p{Punct}[\\u4E00-\\u9FA5]{1,2}\\p{Punct}\\w{5}[\\u4E00-\\u9FA5]{1})" + "|(\\w*?\\p{Punct}[\\u4E00-\\u9FA5]{1,2}\\p{Punct}\\w{6})";
  //需要处理的文件目录。文件名称：2015_hebei_01_01.txt
  val rootPath = "/user/hive/warehouse/highway/year=2015/province=hebei/month=02/day=01/*"

  /**
    * 清洗源数据，分别处理每天的本地文件，将结果文件存储到hdfs
    *
    * //1、使用22个省循环所有文件 2、根据开始结束时间分别处理当前省每天的数据 3、处理完成将数据输出到hdfs
    *
    * @param sc
    */
  def clean(sc: SparkContext): Unit = {
    val lines = sc.textFile(rootPath)
    lines.cache

    //循环所有省数据
    for (province <- Province.haveDataProvinc.keySet) {
      if (!province.equals("hebei")) {}
      else {

        val allDays = DateGenerate.getAllDay(startTime, endTime, province, pattern)

        //处理当前省每天的数据
        val it = allDays.iterator
        while (it.hasNext) {
          val oneDay = it.next
          val year = oneDay.y
          val month = oneDay.m
          val day = oneDay.d

          //删除hdfs目录
          val savePath = "/user/hive/warehouse/highway/year=" + year + "/province=" + province + "/month=" + month + "/day=" + day
          val fileSystem = FileSystem.get(new URI("hdfs://hadoop1:9000"), new Configuration, "spark")
          val isExit = fileSystem.exists(new org.apache.hadoop.fs.Path(savePath))

          if (isExit) {
            fileSystem.delete(new org.apache.hadoop.fs.Path(savePath), true)
            println("删除文件路径：" + savePath)
          }

          lines.map(line => {
            //处理每行数据,更改日志字段
            val newLine = new StringBuilder
            val lineArr = line.split(",")
            //2015-01-01 00:00:00
            val entime = "2015-" + month + "-" + day + " " + randomInt(12, 0) + ":" + randomInt(59, 0) + ":" +
              randomInt(59, 0)
            val exittime = "2015-" + month + "-" + day + " " + randomInt(24, 13) + ":" + randomInt(59, 0) + ":" +
              randomInt(59, 0)

            for (index <- 0 to lineArr.length - 1) {
              if (index == 3) newLine.append(entime).append(",")
              else if (index == 7) newLine.append(exittime).append(",")
              else if (index == lineArr.length - 1) newLine.append(lineArr(index))
              else newLine.append(lineArr(index)).append(",")
            }

            newLine.toString()
          }).saveAsTextFile(savePath)

        }
      }

    }
    lines.unpersist(true)
  }

  /**
    * 月份对应的天数
    */
  def getMonthDay(month: String): Int = {

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
  def randomInt(max: Int, min: Int): String = {
    var res = ""
    val random = new Random();
    val s = random.nextInt(max) % (max - min + 1) + min;
    if (s < 10)
      res = "0" + s
    else
      res = s.toString
    return res;
  }
}

object HighwayImitateDataV2 {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("cleanApplication")
    val sc = new SparkContext(conf)

    val cleanSourceData = new HighwayImitateDataV2;

    cleanSourceData.clean(sc)
    sc.stop();
  }
}
