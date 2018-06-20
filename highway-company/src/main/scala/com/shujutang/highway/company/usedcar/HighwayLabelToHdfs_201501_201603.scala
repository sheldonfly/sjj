package com.shujutang.highway.company.usedcar

import com.shujutang.highway.common.util.{CmdUtil, DateGenerate, FileSystemUtil}

/**
  * 将二手车标签数据导入hive
  *
  * nohup java -classpath highway-company.jar com.shujutang.highway.company.usedcar.HighwayLabelToHdfs_201501_201603 > log2.log 2>&1 &
  *
  * Created by nancr on 2016/9/12.
  */
@deprecated
object HighwayLabelToHdfs_201501_201603{
  def main(args: Array[String]) {
    val startDate = "201501"
    val endDate = "201603"
//    val provinces = "anhui,chongqing,fujian,gansu,guangdong,guangxi,guizhou,hebei,heilongjiang,henan,hubei,hunan," +
//      "jiangsu,jiangxi,jilin,liaoning,ningxia,qinghai,shaanxi,shanxi,shanghai,shandong,sichuan,tianjin," +
//      "yunnan,zhejiang"

    val provinces = "anhui"

//    val hdfsPathForhive = "/hive/wearehouse/highway.db/highway_label"
    val hdfsPathForhive = "/user/hadoop/data/source/highway_label"

    val allMonth = DateGenerate.getAllMonth(startDate, endDate)
    val it = allMonth.iterator

    for (province <- provinces.split(",")){
      val allMonth = DateGenerate.getAllMonth(startDate, endDate)
      val it = allMonth.iterator
      while (it.hasNext){
        //分别处理每个省的数据
        val oneMonth = it.next
        val year = oneMonth.y
        val month = oneMonth.m

        val upPath = s"$hdfsPathForhive/$year/$province/$month"

        val mkhdfsdir = s"hdfs dfs -mkdir -p $upPath"
        if (!FileSystemUtil.isExistHdfs(upPath)) {
           CmdUtil.cmd_result(mkhdfsdir)
        }
        val cmd = s"hdfs dfs -put /mnt/label_data/$province/$year$month/* $upPath"

        try {
          CmdUtil.cmd_result(cmd)
          println("执行指令：" + cmd)
        } catch {
          case e : Exception => e.printStackTrace()
        }
      }
    }
  }
}
