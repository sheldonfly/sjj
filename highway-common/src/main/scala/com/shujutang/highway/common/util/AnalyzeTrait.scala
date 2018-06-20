package com.shujutang.highway.common.util


/**
 * Created by liuxueping on 2016/3/23.
 */
trait AnalyzeTrait{
  var paramsMap:Map[String,String] = Map()

  var saveRootPath = "/data/dmdata/models/"
  val pattern = "yyyyMM"
  val daypattern = "yyyyMMdd"
  var startDate = "201501"
  var endDate = "201512"
  val day = ""
  var productclass = ""
  var provinceList = List[String]()

  //源数据所在目录
  var sourceDataPath = ""

  def printUsage(api: String): Unit = {
    val usage: String = api + "\n" +
      "\n" +
      "Usage: <-s startDate> <-e endDate> <-p province> <-r saveRootPath>\n" +
      "\n" +
      "-s startDate - (string) format pattern yyyyMM eg:201401\n" +
      "-e endDate   - (string) format pattern yyyyMM eg:201506\n" +
      "-p province  - (string) split with ',' eg: hebei,henan\n" +
      "-r saveRootPath  - (string) eg: /user/data/\n" +
      "-productclass -(string) format com.shujutang.highway.StatCarlicenseCount"
      "-sdp -sourceDataPath"
    println(usage)
  }


  //可传递参数指定startDate，endDate，province进行分析
  //TODO:参数验证
  def parseArgs(api: String, args: Array[String]): Unit = {
//    if (args.length%2 != 0) {
//      printUsage(api)
//      System.exit(-1)
//    }

    for(i <- 0 until args.size-1) {
      if (i%2 == 0)
        paramsMap += (args(i) -> args(i+1))
    }

    startDate = paramsMap.getOrElse("-s",startDate)
    endDate = paramsMap.getOrElse("-e",endDate)
    productclass = paramsMap.getOrElse("-productclass", "")
    val pList = paramsMap.getOrElse("-p","all");
    if(!"all".equals(pList)){
      provinceList = pList.split(",").toList
    }
    saveRootPath = paramsMap.getOrElse("-r",saveRootPath)

    sourceDataPath = paramsMap.getOrElse("-sdp",sourceDataPath)

//    if (productclass.eq("")){
//      throw new RuntimeException("IProduct类不能为空!!")
//    }

    print(s"\n-----startDate:$startDate\n-----endDate:$endDate\n-----provinceList:$provinceList\n" +
      s"-----saveRootPath:$saveRootPath\n-----productclass:$productclass")

  }
}