package samples

import com.shujutang.highway.common.domain.{Province, TollStation}

/**
  * Created by wangmeng on 2016/3/31.
  */
class HandlCity {

}

object HandlCity{
  def carLicenseInCity: Unit = {
    scala.io.Source.fromInputStream(HandlCity.getClass.getClassLoader.getResourceAsStream("file/platelocation.txt")).
      getLines().foreach(line => {
      val cparr = line.split(",")
      Province.carLicenseInCity.put(cparr(0),cparr(1))
    })

  }


  def sfzInCity: Unit = {
    scala.io.Source.fromInputStream(HandlCity.getClass.getClassLoader.getResourceAsStream("file/shoufeizhan-detail.txt")).
      getLines().foreach(line => {
      val arr = line.split("\t")
      Province.sfzInCity.put(arr(7), new TollStation(arr(7), arr(3), arr(1), arr(0)))
    })
  }

}