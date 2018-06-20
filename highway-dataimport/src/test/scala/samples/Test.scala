package samples

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by nancr on 2016/1/31.
  */
class Test {

}

object Test{
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("test")
    val sc = new SparkContext(conf)

    val kv1 = sc.parallelize(List(("A",1),("B",2),("C",3),("A",4),("B",5)))

    val log :RDD[String] = sc.textFile("/user/spark/test/log/SogouQ1.txt")
    val s1 = log.map(_.split("\t")).filter(_.length==6).filter(_(3).toInt == 1).filter(_(4).toInt == 2)
    s1.count()

    val rdd2 = log.map(_.split("\t")).filter(_.length==6)
    val rdd3 = rdd2.map(lineWords => (lineWords(1), 1)).reduceByKey(_ + _).map(kv => (kv._2,kv._1)).sortByKey(false).map(words => (words._2, words._1))
    rdd3.toDebugString

    rdd3.take(10)
    rdd3.saveAsTextFile("/user/spark/test/log/out_txt")
  }
}
