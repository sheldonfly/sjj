package samples

import com.shujutang.highway.common.util.RegularUtil
import org.junit._
import Assert._
import scala.util.control.Breaks._

/**
  * Created by nancr on 16/2/2.
  */
object StringTest{
  def main(args: Array[String]) {
    println(("dd", "555"))
  }

  def test1: Unit ={
//	           RKSJ, CKSJ, CKCDBH, CPH, CXDM, CZDM, LC, ZZS, ZXZZ, ZSXS, CHZZ, XZ, CXL, SFLSTD, MFLXDM, LJBS, SFETC, OBU,ZFFS, SFKS, RKZBZDM, CKZBZDM,SFBM,SFMC from ods_sfmxb where part_nian='2015' and part_sf='64ningxia' and part_yue='12' and part_ri='31'; >> /home/catsic/data/ningxia/2015_ningxia_12_31.txt
   val line = "000000000005000\t000000000000702\t2016-03-31 10:15:15\t000000000005000\t000000000000401\t2016-03-31 10:26:17\t渝AWV234\t01\t0\t21.15\t0\t\t0.0\t0.0\t105.0 D6D8C7EC01035963\t1\t01\t50Chongqing\t重庆"
    val words = line.split("\t")
    println("---" + words(5).length + "---")
  }

  def test2: Unit ={
    val car = "豫SL5643"
    println(car.substring(0, 2))
  }

  def tes3(): Unit ={
    var a = 12
    if (a < 10){
      println(a)
      return ()
    }

    a = 11
    println(a)
  }
}
