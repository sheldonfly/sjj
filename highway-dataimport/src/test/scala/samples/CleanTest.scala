package samples

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by nancr on 2016/3/25.
  */
object CleanTest {

	def main(args: Array[String]) {
		val conf = new SparkConf().setAppName("cleantest").setMaster("local")
		val sc = new SparkContext(conf)

		val res = sc.textFile("D:/tmp/2015_jiangsu_01_01.txt").map(line => {
			try {
				val words = line.split("\t")
//				println(words(3))
				words.foreach(println(_))
				words(3)
			} catch {
				case e:Exception =>{println(line)}
			}
		}).collect()
//		println(res.foreach(word => println(word)))
		sc.stop
	}

}
