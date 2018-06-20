package samples

import com.shujutang.highway.common.domain.{Province, TollStation}

/**
  * Created by wangmeng on 2016/3/31.
  */
class MapPutTest {

}

object MapPutTest{

  def main(args: Array[String]) {
    val m = MapPutTest
    HandlCity.carLicenseInCity
    HandlCity.sfzInCity
    println("测试是否取到carLicenseInCity中的数据"+Province.carLicenseInCity.size)
    println("测试是否取到sfzInCity中的数据"+Province.sfzInCity.size)
    println("津AFJ520".length)

    m.readnew

//    val carlicense = "辽HL1920"
//    val localtion = Province.map.getOrElse(carlicense.substring(0, 2),"nocity")
//
//    println("取到了辽F所对应的地址："+ localtion)
  }

  def readnew:Unit = {
    scala.io.Source.fromInputStream(HandlCity.getClass.getClassLoader.getResourceAsStream("file/new1.txt")).
      getLines().foreach(line => {
      val words = line.split("\t")
      val carlicense = words(3)

      if(words.length==24) {
        println("输出取到的车牌号" + carlicense)
        println(carlicense.substring(0, 2))
        var cityNameByCar = Province.carLicenseInCity.getOrElse(carlicense.substring(0, 2), "nocity")
        println("测试map中车牌所在地是否能取到：" + cityNameByCar)
        var cityNameByEnter = Province.sfzInCity.getOrElse(words(words.length - 4), new TollStation()).sfzmc
        println("测试入口是否能取到：" + cityNameByEnter)
        var cityNameByExit = Province.sfzInCity.getOrElse(words(words.length - 3), new TollStation()).sfzmc
        println("测试出口是否能取到：" + cityNameByExit)
        var enterProvice = Province.sfzInCity.getOrElse(words(words.length - 4), new TollStation()).province
        println("测试入口省是否能取到：" + enterProvice)
        var exitProvince = Province.sfzInCity.getOrElse(words(words.length - 3), new TollStation()).province
        println("测试出口省是否能取到：" + exitProvince)
      }
    })

  }

}
