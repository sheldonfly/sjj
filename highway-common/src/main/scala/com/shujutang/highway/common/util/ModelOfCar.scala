package com.shujutang.highway.common.util

import scala.collection.mutable
import scala.io.Source

/**
  * 通过车型代码，得到车型名称或描述信息
  *
  * Created by nancr on 2016/10/26.
  */
object ModelOfCar {
  def main(args: Array[String]) {
    println(carmodelMap)
  }

  var carmodelMap = new mutable.HashMap[String, CarModel]()
  Source.fromInputStream(ModelOfCar.getClass.getClassLoader.getResourceAsStream("carmodel.txt")).getLines()
    .foreach(line =>{
      val arr = line.split(",")
      val province = arr(1)
      val modelcode = arr(2)
      val modelname = arr(3)
      var modeldefine = arr(4)

      carmodelMap += (s"$province$modelcode" -> CarModel(modelname, modeldefine))
    })

  def getModelName(province: String, modelcode: String): String ={
    val key = s"$province$modelcode"
    try {
      carmodelMap.get(key).get.modelname
    }catch {
      case e: Exception => "none"
    }
  }

  def getModelDefine(province: String, modelcode: String): String = {
    val key = s"$province$modelcode"
    try {
      carmodelMap.get(key).get.modeldefine
    }catch {
      case e:Exception => "none"
    }
  }

}

case class CarModel(modelname: String, modeldefine: String)