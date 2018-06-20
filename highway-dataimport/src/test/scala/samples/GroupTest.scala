package samples

import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by nancr on 2016/2/23.
  */
class GroupTest {

}

object GroupTest{
  def main(args: Array[String]) {
    val conf = new SparkConf
    val sc = new SparkContext(conf)

    val list = sc.parallelize(List(("1,aaa,bbb"), ("1,ccc,ddd"), ("1,eee,fff"),("2,aaa,bbb"), ("2,ccc,ddd"), ("2,eee,fff")))

    val linearr = list.map(line => {line.split(",")})

    val group1 = linearr.groupBy(arr =>{arr(0)})

    val firstgroup = group1.filter(map => map._1.equals("1")).map({_._2})

    val arrmap = firstgroup.map(itarr => {
      var list = new java.util.ArrayList[Map[String, String]]()
      val it = itarr.iterator
      while (it.hasNext){
        val lineArr = it.next
        val map = Map("aa"->lineArr(0), "bb"->lineArr(1),"cc"->lineArr(2))

        list.add(map)
      }
      list
    })

//    EsSpark.saveToEs(arrmap, "indextest/aaa")

  }
}
