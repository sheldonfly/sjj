package com.shujutang.highway.dataimport.check

import java.io.{FileWriter, PrintWriter}

import com.shujutang.highway.common.domain.Province
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by wangmeng on 2016/7/28.
  */
object DataSample {
  var samplefile: PrintWriter = null
  val filePath = "/user/hadoop/highway"
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("sampledata").setMaster("spark://hadoop160:7077")
    val sc = new SparkContext(conf)
    for (province <- Province.haveDataProvinc.keySet) {
      samplefile = new PrintWriter(new FileWriter("2016_"+province+"_sampledata.csv"), true)
      println("当前省：************************"+province)
      val filename = "2016_"+province+"_03_*";
      val totalPath = s"${filePath}/2016/$province/$filename"

      println(s"################文件路径：$totalPath")

      val files = sc.textFile(totalPath)
//      val totalCount = files.count()
//      println("三月份总量为"+totalCount+"条")

      val resultrdd = files.takeSample(false,20000)

      resultrdd.map(line=>{line.replace("\t",",")}).foreach(samplefile.println(_))

      samplefile.flush()
      samplefile.close()
    }

    sc.stop()

  }
}
