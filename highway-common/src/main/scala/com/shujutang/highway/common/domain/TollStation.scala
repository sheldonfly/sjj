package com.shujutang.highway.common.domain

/**
  * 收费站
  *
  * @param sfzbh 收费站编号
  * @param sfzmc 收费站名称
  * @param cityname 城市名称
  * @param province 省份
  *
  * Created by nancr on 2016/3/18.
  */
class TollStation(val sfzbh: String="", val sfzmc: String="none", val cityname: String="none", val province: String="none") {

//	def this(){
//		this("", "noname", "nocity", "nopro")
//	}

}
