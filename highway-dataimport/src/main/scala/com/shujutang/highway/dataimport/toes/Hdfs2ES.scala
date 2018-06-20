package com.shujutang.highway.dataimport.toes

import java.net.URI
import java.util.Date

import com.shujutang.highway.common.domain.Province
import com.shujutang.highway.common.util.DateGenerate
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.spark.{SparkContext, SparkConf}

/**
  * 将源数据从hdfs中取出
  * 分组
  * 存储到对应的es type中
  *
  * spark-submit --master spark://master:7077 --executor-memory 12g --class com.shujutang.highway.dataimport.toes.Hdfs2ES highway-dataimport-1.0.jar
  * Created by nancr on 2016/2/18.
  */
object Hdfs2ES {
  def main(args: Array[String]) {
    //指定开始结束时间 如201401 201412

    //循环所有省数据（22个）
    ////循环每个月数据
    //////1、读取文件，2、过滤掉无效数据，处理车牌为标准车牌 3、缓存当前月数据 4、分别用33个省循环判断是否属于当前省记录，保存到es 5、删除缓存
    val startDate = "201501"
    val endDate = "201501"
    val pattern = "yyyyMM"
    val rooPath = "/user/spark/clean/highway/"

    //
    val conf = new SparkConf()
    conf.set("es.index.auto.create", "true")
    conf.set("es.nodes", "192.168.3.72:9600,192.168.3.73:9600,192.168.3.74:9600")
    val sc = new SparkContext(conf)

    val fileSystem = FileSystem.get(new URI("hdfs://master:9000"), new Configuration, "spark")


    //循环所有省
    for(province <- Province.haveDataProvinc.keySet){
      val sDate = new Date()
      println("**********************开始将：【" + province + "】省的数据导入ES**********************")
      //时间段
      val times = DateGenerate.getAllMonth(startDate, endDate, pattern)

      //循环各个省数据
      val it = times.iterator
      while (it.hasNext){
        val oneMonth = it.next

        //年月
        val year = oneMonth.y
        val month = oneMonth.m


        val filePath1 = rooPath + "year=" + year + "/province=" + province + "/month=" + month
        //判断目录是否存在
        val isExit = fileSystem.exists(new org.apache.hadoop.fs.Path(filePath1))
        if (isExit){
          println("########导入 年：" + year + " 月：" + month + " 的数据#########")
          //hdfs文件路径
          val filePath = rooPath + "year=" + year + "/province=" + province + "/month=" + month + "/day=*/"
          //当前月有效数据
          val defTime = "1970-01-01 00:00:00"
          val monthRDD = sc.textFile(filePath).map(line => {line.split(",")}).filter(lineArr => {lineArr(30).equals("0")}).map(lineArr => {var carlicense = lineArr(9);
            val  len = carlicense.length;carlicense = carlicense.substring(len - 7, len);lineArr(9) = carlicense; val entime = lineArr(3); if(entime.equals("0000-00-00 00:00:00")) lineArr(3) = defTime;
            val exittime = lineArr(7); if (exittime.equals("0000-00-00 00:00:00")) lineArr(7) = defTime; lineArr})
          //缓存monthRDD
          monthRDD.cache()

          //将当前月数据分组，分别放到对应的省份
          for ((k, v) <- Province.allProvince){
            //当前省数据
              val groupRDD = monthRDD.filter(lineArr => {lineArr(9).startsWith(v)}).map(lineArr => {
                val map = Map("enstacd"->lineArr(1), "entname"->lineArr(2),"entime"->lineArr(3),"exitstacd"->lineArr(5),"exitname"->lineArr(6),
                  "carlicense"->lineArr(9),"transproflg"->lineArr(26),"vhcmodel"->lineArr(10),"vhctype"->lineArr(11),"weight"->lineArr(17),
                  "limitweight"->lineArr(18),"mileage"->lineArr(12),"month"->lineArr(3).split(" ")(0), "overload"->lineArr(32),"overspeed"->lineArr(31))
                map
              })

            //当前省当前月数据插入es，es存储方式 province/yearmonth (index/type)
//            EsSpark.saveToEs(groupRDD, k + "/" + year + month)
            println("########分组 省：" + k + " " + v +  "完成#########")
          }

          //删除缓存
          monthRDD.unpersist()
        }

      }

      val eDate = new Date()
      println("************完成将：【" + province + "】省的数据导入ES**********用时：" + (eDate.getTime - sDate.getTime)/1000/60 + "分钟")
    }

  }
}
