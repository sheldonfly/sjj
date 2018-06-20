package com.shujutang.highway.company.usedcar

import com.shujutang.highway.common.util._
import org.apache.hadoop.fs.Path

/**
  *nohup spark-submit --master spark://hadoop1:7077 --driver-memory 3072M --executor-memory 6g --executor-cores 5 --total-executor-cores 50 --class com.shujutang.highway.company.usedcar.HdfsToHive /home/hadoop/file/jar/highway_company_jar/highway-company.jar -s 201501 -e 201607 -p chongqing,fujian,gansu,guangdong,guangxi,guizhou,hebei,heilongjiang,henan,hubei,hunan,jiangsu,jiangxi,jilin,liaoning,ningxia,qinghai,shaanxi,shanxi,shanghai,shandong,sichuan,tianjin,yunnan,zhejiang > log2hive.log 2>&1 &
  *
  * Created by nancr on 2016/10/8.
  */
@deprecated
object HdfsToHive extends ContextUtil with AnalyzeTrait{
  def main(args: Array[String]) {
    parseArgs("HdfsToHive", args)

//    val startDate = "201501"
//    val endDate = "201607"
//    val provinces = "anhui,chongqing,fujian,gansu,guangdong,guangxi,guizhou,hebei,heilongjiang,henan,hubei,hunan," +
//      "jiangsu,jiangxi,jilin,liaoning,ningxia,qinghai,shaanxi,shanxi,shanghai,shandong,sichuan,tianjin," +
//      "yunnan,zhejiang"

    val hivePath = "/hive/wearehouse/highway.db/highway_label"
    val hdfsPathForhive = "/user/hadoop/data/source/highway_label"

    val allMonth = DateGenerate.getAllMonth(startDate, endDate)
    val it = allMonth.iterator

    for (province <- provinceList) {
      val allMonth = DateGenerate.getAllMonth(startDate, endDate)
      val it = allMonth.iterator
      while (it.hasNext) {
        //分别处理每个省的数据
        val oneMonth = it.next
        val year = oneMonth.y
        val month = oneMonth.m

        val savePath = s"$hivePath/year=$year/province=$province/month=$month"
        if (FileSystemUtil.isExistHdfs(savePath)) {
          FileSystemUtil.delHdfs(savePath)
        }
        println(s"保存路径：$savePath")

        sc.textFile(s"$hdfsPathForhive/$year/$province/$month/*").repartition(1)
          .saveAsTextFile(savePath)
      }
    }
    sc.stop()
  }
}
