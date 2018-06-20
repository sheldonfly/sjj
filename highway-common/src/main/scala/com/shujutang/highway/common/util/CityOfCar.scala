package com.shujutang.highway.common.util

import java.io.PrintWriter

import com.shujutang.highway.common.domain.Province

import scala.StringBuilder
import scala.collection.mutable
import scala.io.Source

/**
  * 通过车牌前两个字符得到车牌所属省或市
  *
  * Created by nancr on 2016/10/25.
  */
object CityOfCar {
  val filePath = "cityofcars2.txt"
  var tollMap = new mutable.HashMap[String, ProvinceCity]()

  Source.fromInputStream(CityOfCar.getClass.getClassLoader.getResourceAsStream(filePath)).getLines().foreach(line=>{
    val ws = line.split(",")
//    if (ws.length == 3){
      tollMap += (ws(0) -> ProvinceCity(ws(1), ws(2)))
//    }
  })

  /**
    * 通过车牌前两个字符得到车牌所属省
    * @param carLicense
    * @return
    */
  def getProvince(carLicense: String):String={
    try{
      tollMap.get(carLicense.substring(0,2)).get.province
    }catch {
      case e: Exception => "none"
    }
  }

  /**
    * 通过车牌前连个字符得到车牌所属市
    * @param carLicense
    * @return
    */
  def getCity(carLicense: String): String = {
    try {
      tollMap.get(carLicense.substring(0,2)).get.city
    }catch {
      case e: Exception => "none"
    }
  }

  def getCityWithProvince(carLicense: String): String = {
    try {
      val city= tollMap.get(carLicense.substring(0,2)).get.city
      val province = tollMap.get(carLicense.substring(0,2)).get.province
      s"$province$city"
    }catch {
      case e: Exception => "none"
    }
  }

  def main(args: Array[String]) {

    print(tollMap)

//    handleData
  }

  /**
    * 处理cityofcars2.txt文件
    */
  def handleData: Unit = {
    val file = new PrintWriter("/Users/nancr/work/tmp/cityofcars2.txt")

    val citys = mutable.Set("北京", "上海", "重庆")

    var res = new StringBuilder //车牌,省,市
    Source.fromInputStream(CityOfCar.getClass.getClassLoader.getResourceAsStream(filePath)).getLines().foreach(line => {
      val lineArr = line.split(",")
      val carlicense = lineArr(0)
      val car1 = carlicense.replaceAll("\uFEFF", "").substring(0, 1)

      val province = Province.provinceMap2.get(car1).get

      val pc = lineArr(1)
      val provinceLen = province.length
      var city = pc.substring(pc.length - provinceLen, pc.length)
      //        if(citys.contains(city))
      //          city = ""
      res = res.append(s"$carlicense,$province,$city\n")
    }
    )

    file.print(res)
    file.close()
  }

}

case class ProvinceCity(province: String, city: String)
