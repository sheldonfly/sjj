package com.shujutang.highway.company.usedcar

import com.shujutang.highway.common.util.{AnalyzeTrait, CmdUtil, DateGenerate}

/**
  * 将二手车标签数据导入hive
  * 本地文件-》hive
  *
  * nohup java -classpath highway-company.jar com.shujutang.highway.company.HighwayLabelToHive 201501  201603
  *  chongqing,fujian,gansu,guangdong,guangxi,guizhou,hebei,heilongjiang,henan,hubei,hunan,jiangsu,jiangxi,
  * jilin,liaoning,ningxia,qinghai,shaanxi,shanxi,shanghai,shandong,sichuan,tianjin,yunnan,zhejiang
  * > hlth.log 2>&1 &
  *
  * Created by nancr on 2016/9/12.
  */
object LocalFileToHive{
  def main(args: Array[String]) {
    val startDate = args(0)
    val endDate = args(1)
    val provinceList = args(2).split(",").toList

    localtohive(startDate, endDate, provinceList)
  }

  def localtohive(startDate: String, endDate: String, provinceList: List[String]): Unit = {
    val hdfsPathForhive = "/hive/wearehouse/highway.db/highway_label"

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

        val upPath = s"$hdfsPathForhive/year=$year/province=$province/month=$month"

        val cmd = s"hdfs dfs -put /mnt/label_data/$province/$year$month/* $upPath"

        try {
          println("执行指令：" + cmd)
          CmdUtil.cmd_result(cmd)
        } catch {
          case e: Exception => e.printStackTrace()
        }
      }
    }
  }
}
