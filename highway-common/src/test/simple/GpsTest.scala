package simple

import com.shujutang.highway.common.util.GpsUtil
import org.junit.Test

/**
  * Created by nancr on 2016/5/10.
  */
object GpsTest {
  def main(args: Array[String]) {
//    val gps = Array("124.423591,43.110112", "126.12591,45.122508", "124.407398,43.094271", "119.875998,40.076124",
//      "126.15203,45.173923")
//
//    val newarr = gps.combinations(2)
//    while (newarr.hasNext){
//      val gpss = newarr.next
////      println(gpss.mkString("\t"))
//      val lat_a = gpss(0).split(",")(1).toDouble
//      val lng_a = gpss(0).split(",")(0).toDouble
//      val lat_b = gpss(1).split(",")(1).toDouble
//      val lng_b = gpss(1).split(",")(0).toDouble
//      println(GpsUtil.gps2m(lat_a, lng_a, lat_b, lng_b))
//    }
    test1
  }

  @Test
  def test1: Unit ={
    val gps1 = "119.188893,31.599973"
    val gps2 = "112.546586,34.715326"
    val lat_a = gps1.split(",")(1).toDouble
    val lng_a = gps1.split(",")(0).toDouble
    val lat_b = gps2.split(",")(1).toDouble
    val lng_b = gps2.split(",")(0).toDouble
    println(GpsUtil.gps2m(lat_a, lng_a, lat_b, lng_b))
  }
}
