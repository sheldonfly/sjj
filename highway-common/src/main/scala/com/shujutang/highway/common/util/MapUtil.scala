package com.shujutang.highway.common.util

/**
  * Created by nancr on 2016/12/16.
  */
object MapUtil {

  def getCarModel(modelMap: Map[String, String], carmodel: String): String ={
    try {
      modelMap.get(carmodel).get
    } catch {
      case e: Exception => "none"
    }
  }
}
