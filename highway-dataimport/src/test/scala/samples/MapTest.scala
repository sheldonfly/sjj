package samples

import com.shujutang.highway.common.domain.TollStation

import scala.collection.mutable

/**
  * Created by nancr on 2016/3/25.
  */
class MapTest {

}

object MapTest{
	def main(args: Array[String]) {
		//收费站和城市的映射
		val sfzInCity = new mutable.HashMap[String, TollStation]()
		sfzInCity.put("aaa", new TollStation("1", "2", "3eee", "4"))

		val res = sfzInCity.get("aaa").getOrElse(new TollStation())
		println(res.cityname)
	}
}
