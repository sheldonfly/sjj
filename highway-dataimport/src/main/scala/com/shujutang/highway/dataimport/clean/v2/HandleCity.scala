package com.shujutang.highway.dataimport.clean.v2

import com.shujutang.highway.common.domain.{TollStation, Province}
import com.shujutang.highway.common.util.JdbcUtil

/**
  * 处理 车牌所在市
  *
  * Created by nancr on 2016/3/18.
  */
class HandleCity {

  /**
    * 创建车牌和城市的映射
    */
//  def carLicenseInCity: Unit = {
//    val sql = "select * from LICENSE_PLATE"
//    val url = "jdbc:mysql://hadoop1:3306/test?characterEncoding=UTF-8&" +
//      "zeroDateTimeBehavior=convertToNull"
//    val username = "root"
//    val password = "Simple1921"
//
//    val conn = JdbcUtil.getDBConn(url, username, password)
//
//
//    val list = JdbcUtil.qu4ListAssignDb(conn, sql)
//
//    val it = list.iterator()
//    while (it.hasNext) {
//      val map = it.next
//      Province.carLicenseInCity.put(map.get("sortNumber").toString, map.get("location").toString)
//    }
//  }
  def carLicenseInCity: Unit = {
    scala.io.Source.fromInputStream(HandleCity.getClass.getClassLoader.getResourceAsStream("file/cityofcars.txt")).
      getLines().foreach(line => {
      val cparr = line.split(",")
      Province.carLicenseInCity.put(cparr(0),cparr(1))
    })
  }

  /**
    * 创建收费站和城市的映射
    */

  def sfzInCity: Unit = {
      scala.io.Source.fromInputStream(HandleCity.getClass.getClassLoader.getResourceAsStream("file/dim_sfz.txt")).
        getLines().foreach(line => {
        val arr = line.split(",")
        val sfzbh = arr(1)
        val sfzmc = arr(3)
        val wlbh = arr(2)
        val sfbm = arr(0)
        val sfmc = arr(4)
        val city = arr(5)
        Province.sfzInCity.put(sfbm+sfzbh+wlbh, new TollStation(sfzbh, sfzmc, city, sfmc))
      })
    }
}


object HandleCity {
  def main(args: Array[String]) {
    val handleCity = new HandleCity
    handleCity.sfzInCity
  }


  def carLicenseInCity: Unit = {
    val handleCity = new HandleCity
    handleCity.carLicenseInCity
  }

  def sfzInCity: Unit = {
    val handleCity = new HandleCity
    handleCity.sfzInCity
  }
}
