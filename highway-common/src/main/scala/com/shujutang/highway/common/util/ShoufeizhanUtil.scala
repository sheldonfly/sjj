package com.shujutang.highway.common.util

import scala.collection.mutable
import scala.io.Source

/**
 * 收费站
 * Created by liuxueping on 2016/5/13.
 */
object ShoufeizhanUtil extends Serializable{

  val filePath = "dim_sfz.txt"
  var tollMap = new mutable.HashMap[String, Station]()

  Source.fromInputStream(ShoufeizhanUtil.getClass.getClassLoader.getResourceAsStream(filePath)).getLines().foreach(line=>{
//    println(line)
    val ws = line.split(",")
    if (ws.length == 7){
      val b = if(ws(6).equals("1")) true else false
      tollMap += (ws(0) -> Station(ws(1), ws(2), ws(3), ws(4), ws(5), b))
    }
  })


  def main(args: Array[String]) {

//    println(tollMap.size)
//    println(getStationNameWithPrinceCity("guangdong441200000000106000000000000001"))
    println(getCityWithProvince("yunnan000000000000053000000000087002"))
  }

  /**
    * 获取收费站名称
    *
    * @param code
    * @return
    */
  def getStationName(code: String): String ={
    try {
      tollMap.get(code).get.name
    } catch {
      case e: Exception => "none"
    }
  }

  /**
    * 获取收费站名称-带省份和城市
    *
    * @param code
    * @return
    */
  def getStationNameWithPrinceCity(code: String): String ={
    try {
      val pc = getCityWithProvince(code)
      pc + tollMap.get(code).get.name
    } catch {
      case e: Exception => "none"
    }
  }

  /**
   * 获取收费站所属的市
    *
    * @param code
   * @return
   */
  def getCity(code: String): String = {

    try {
      tollMap.get(code).get.city
    }
    catch {
      case e => "none"
    }
  }

  val shengjiCity = Set("上海","天津","北京", "重庆")
  /**
    * 获取城市名称-带省份
    *
    * @param code
    * @return
    */
  def getCityWithProvince(code: String): String = {

    try {
      val province = getProvince(code)
      if (shengjiCity.contains(province)){
        tollMap.get(code).get.city
      }else{
        province + tollMap.get(code).get.city
      }

    }
    catch {
      case e => "none"
    }
  }

  /**
   * 获取收费站所属的省
    *
    * @param code 编号
   * @return
   */
  def getProvince(code: String): String = {

    try {
      tollMap.get(code).get.province
    }
    catch {
      case e => "none"
    }
  }

  /**
   * 获取收费站所属的路线
    *
    * @param code 编号
   * @return
   */
  def getRoad(code: String): String = {

    try {
      tollMap.get(code).get.road
    }
    catch {
      case e => "none"
    }
  }

  /**
    * 获取收费站所属的路线
    *
    * @param code 编号
    * @return
    */
  def getStationLineName(code: String): String = {

    try {
      tollMap.get(code).get.stationLineName
    }
    catch {
      case e => "none"
    }
  }


  /**
   * 收费站是否跨省
    *
    * @param code 编号
   * @return
   */
  def isBorder(code: String): Boolean = {

    try {
      tollMap.get(code).get.border
    }
    catch {
      case e => false
    }
  }


}

/**
 * 收费站
  *
  * @param name 收费站名称
 * @param province 收费站所属省
 * @param city 收费站所属市
 * @param road 收费站所属路线
 * @param border 是否跨界
 */
case class Station(name: String, province: String, city: String, road: String, stationLineName: String,
                   border: Boolean)