package com.shujutang.highway.common.util

import java.io.FileWriter

import com.shujutang.highway.common.domain.Province

import scala.io.Source

/**
  * Created by nancr on 2016/5/12.
  */
object HandleShoufeizhan {
  def main(args: Array[String]) {
    val filePath = "d:/tmp/dim_shoufeizhan.txt"
    val outFile = "d:/tmp/dim_sfz.txt"
    val pw = new FileWriter(outFile, true)

    val shengjiCity = Set("上海","天津","北京", "重庆")

    //收费站编号对应的名称
    Source.fromFile(filePath).getLines().map(line => {
      val ws = line.split(",")
      val sfName = ws(0)
      val provinceName = Province.name2Number.getOrElse(sfName, "null")
      val wlbh = ws(8)
      val sfzbh = ws(9)
      var xlmc = ws(13)
      val xldm = ws(14)
      var stationName = ws(10)
      if (!stationName.contains("收费站")){
        if (stationName.contains("站")){
          stationName = stationName.replaceAll("站", "收费站")
        }else{
          stationName += "收费站"
        }
      }

      if (stationName.contains(xldm)){
        stationName = stationName.replaceAll(xldm, "")
      }
      if (xlmc.contains(xldm)){
        xlmc = xlmc.replaceAll(xldm, "")
      }

      var city = ws(1).replace("市","").trim
      if (shengjiCity.contains(sfName)){
        city = sfName
      }

      val shengjie = ws(12)


      val res = s"${provinceName}$wlbh$sfzbh,$stationName,$sfName,$city,$xldm,$xlmc,$shengjie\n"
      pw.write(res)
    }).length

    pw.flush()
    pw.close()
  }
}
