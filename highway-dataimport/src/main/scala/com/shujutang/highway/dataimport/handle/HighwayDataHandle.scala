package com.shujutang.highway.dataimport.handle

import java.io.{FileWriter, PrintWriter}

import com.shujutang.highway.common.util.ShoufeizhanUtil

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
  * Created by nancr on 2016/6/8.
  */
object HighwayDataHandle {
  val filePath = "D:/tmp/2016_fujian_02_01.txt"
  def main(args: Array[String]) {
    val province = "fujian"
    val pw = new PrintWriter(new FileWriter("D:/tmp/fuzou.txt"))
    Source.fromFile(filePath).getLines().filter(!_.contains("chzz")).map(_.split("\t")).filter(_.length == 23).map(words => {
      val rkwlbh = words(0).trim
      val rkzbh = words(1).trim

      val ckwlbh = words(3).trim
      val ckzbh = words(4).trim

      //入口和出口的编号
      val enterStarionNum = s"$province$rkwlbh$rkzbh"
      val outStationNum = s"$province$ckwlbh$ckzbh"

      //入口和出口的名称
      val enterName = ShoufeizhanUtil.getCity(enterStarionNum)
      val outName = ShoufeizhanUtil.getCity(outStationNum)

      val ab = new ArrayBuffer[String]
      ab ++= words
      ab += (enterName, outName)
      (ab, words)
    }).filter(words => {
      words._1(23).equals("福州市") || words._1(24).equals("福州市")
    }).foreach(words => {
      val line = words._2.mkString("\t")
      pw.println(line)
    })

    pw.flush()
    pw.close()
  }

}
