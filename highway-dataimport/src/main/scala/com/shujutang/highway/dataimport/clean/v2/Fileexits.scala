package com.shujutang.highway.dataimport.clean.v2

import java.net.URI

import com.shujutang.highway.common.domain.Province
import com.shujutang.highway.common.util.DateGenerate
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by wangmeng on 2016/4/6.
  */
class FileExits extends java.io.Serializable{
  val startTime = "20140101"
  val endTime = "20141231"
  val pattern = "yyyyMMdd"
  @transient
  val conf = new Configuration
  val uri = "/user/hive/warehouse/highway/year=2014"
  val rootPath = "/user/hive/warehouse/highway/year=2014/"

  def exit():Unit={
    for (province <- Province.haveDataProvinc.keySet) {
      val allDays = DateGenerate.getAllDay(startTime, endTime, province, pattern)
      //处理当前省每天的数据
      val it = allDays.iterator
      while (it.hasNext) {
        val oneDay = it.next
        val year = oneDay.y
        val month = oneDay.m
        val day = oneDay.d

        val cleanPath1 = rootPath + "province=" + province + "/month=" + month + "/day=" + day + "/part-00000"
        val cleanPath2 = rootPath + "province=" + province + "/month=" + month + "/day=" + day + "/part-00001"

        val hdfs = FileSystem.get(URI.create(uri), conf, "spark")
        val isExit1 = hdfs.exists(new Path(cleanPath1))
        val isExit2 = hdfs.exists(new Path(cleanPath1))
        if(!isExit1 || !isExit2){
          println("某个文件不存在：" + cleanPath1 + "或者"+ cleanPath2)
        }
      }
    }
  }
}

object FileExits{
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("fileExitsApplication")
    val sc = new SparkContext(conf)

    val fileExits = new FileExits

    fileExits.exit()
    sc.stop();
  }
}
