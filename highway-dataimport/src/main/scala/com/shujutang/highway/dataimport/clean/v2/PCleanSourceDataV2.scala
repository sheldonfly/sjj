package com.shujutang.highway.dataimport.clean.v2

import java.net.URI
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import java.util.regex.Pattern

import com.shujutang.highway.common.domain.{Festival, Province, TollStation}
import com.shujutang.highway.common.util.{DateGenerate, RegularUtil}
import com.shujutang.highway.dataimport.clean.v2.HandleCity
import org.apache.commons.lang.time.DateUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 清洗原始数据,清洗完成将数据输出到hdfs的指定目录，共hive分区表使用
  *
  * ## jar执行时，不指定master参数 ##
  *
  * 1: 数据是否无效,并添加标识:
  * 0：正常数据，
  * 1：错误车牌
  *
  * 2:  超载：1 未超载：0
  *
  * 3:  超速：1 未超速：0
  * select RKSJ, CKSJ, CKCDBH, CPH, CXDM, CZDM, LC, ZZS, ZXZZ, ZSXS, CHZZ, XZ, CXL, SFLSTD, MFLXDM, LJBS, SFETC, OBU,ZFFS,
  * SFKS, RKZBZDM, CKZBZDM,SFBM,SFMC
  * from ods_sfmxb where part_nian='2015' and part_sf='45guangxi' and part_yue='01' and part_ri='09';
  *
  * 执行:  nohup spark-submit --executor-memory 5g --total-executor-cores 24 --executor-cores 3 --master spark://hadoop160:7077 --class com.shujutang.highway.dataimport.clean.v2.CleanSourceDataV2  highway-dataimport-1.0.jar
  * Created by nancr on 16/1/28.
  */
class PCleanSourceDataV2 extends java.io.Serializable{
  val startTime = "20160101"
  val endTime = "20160630"
  var datePattern = Array("yyyy-MM-dd HH:mm:ss")
  var separator = "\t"
  val pattern = "yyyyMMdd"
  val reg = "(\\w*[\\u4E00-\\u9FA5]{1,2}\\w{6})" + "|(\\w*?[\\u4E00-\\u9FA5]{1}\\w{5}[\\u4E00-\\u9FA5]{1})" + "|(\\w*?\\p{Punct}[\\u4E00-\\u9FA5]{1,2}\\p{Punct}\\w{5}[\\u4E00-\\u9FA5]{1})" + "|(\\w*?\\p{Punct}[\\u4E00-\\u9FA5]{1,2}\\p{Punct}\\w{6})";
  val regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）_ - - +|{}【】‘；：”“’ \" \" 。，、？]"
  val eL = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$"
  val eL1 = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])\\s+00:00:00$"
  val df = new SimpleDateFormat("yyyy-MM-dd")
  val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")


  @transient
  val conf = new Configuration
  //需要处理的文件目录。文件名称：2015_hebei_01_01.txt
  //  val uri = "hdfs://hadoop3:8020"
  val uri = "/user/hadoop/highway/2016"
  val rootPath = "/user/hadoop/highway/2016/"

  /**
    * 清洗源数据，分别处理每天的本地文件，将结果文件存储到hdfs
    *
    * //1、使用22个省循环所有文件 2、根据开始结束时间分别处理当前省每天的数据 3、处理完成将数据输出到hdfs
    *
    * @param sc
    */
  def clean(sc: SparkContext): Unit = {
    //    val sqlContext = new SQLContext(sc)
    //    import sqlContext.implicits._
    //    val schema = Schema.getSchema()

    HandleCity.sfzInCity
    HandleCity.carLicenseInCity

    val broadcastvar = sc.broadcast(Province.sfzInCity)

    val broadcastvar2 = sc.broadcast(Province.carLicenseInCity)

//    val sfzInCity = broadcastvar.value
//
//    val carLicenseInCity = broadcastvar2.value

    //循环所有省数据
    for (province <- Province.haveDataProvinc.keySet) {

      //TODO
      println("当前省=" + province)

      val allDays = DateGenerate.getAllDay(startTime, endTime, province, pattern)

      //处理当前省每天的数据
      val it = allDays.iterator
      while (it.hasNext) {
        val oneDay = it.next
        val year = oneDay.y
        val month = oneDay.m
        val day = oneDay.d
        //需要处理的文件路径
        val cleanFilePath = rootPath +province+"/"+ year + "_" + province + "_" + month + "_" + day + ".txt"
        //        val file = new File(cleanFilePath)
        //        val isExit = file.exists
        // TODO 不应该每次都创建一个FileSystem ，参考 com.shujutang.highway.dm.util.FileSystemUtil
        val hdfs = FileSystem.get(URI.create(uri), conf, "spark")
        val isExit = hdfs.exists(new Path(cleanFilePath))

        val savePath = "/user/hive/warehouse/highway/" + "year=" + year + "/province=" + province + "/month=" + month + "/day=" + day
        if(hdfs.exists(new Path(savePath))){
          hdfs.delete(new Path(savePath), true)
        }
        if (isExit){
          try {

            println("文件：" + cleanFilePath + "     文件是否存在：" + isExit)
            sc.textFile(cleanFilePath,200).map(f = line => {
              var res = "";
              val sbf = new StringBuilder
              val words = line.split("\t")

              val sfzInCity = broadcastvar.value

              val carLicenseInCity = broadcastvar2.value
              //
              //  println("当前行=" + line + "      单前行的长度=" + words.length)
              if (words.length == 23 && !(words.contains("chzz"))) {

                // 车牌是否有效
                // var carlicense = words(6).replaceAll("\"","").trim.toUpperCase()
               var carlicense = words(6)
                val pt = Pattern.compile(regEx)
                val m = pt.matcher(carlicense)

                var newcph = m.replaceAll("").trim().toUpperCase()

                if (newcph.startsWith("黑黑")){
                  carlicense = newcph.replaceAll("^黑|[黑蓝黄白天棕]$","")
                  words(6) = carlicense
                }else{
                  carlicense = newcph.replaceAll("^[黑蓝黄白天棕]|[黑蓝黄白天棕]$","")
                  words(6) = carlicense
                }

                val isMatch = RegularUtil.`match`(carlicense, RegexUtil.RegEx)

                if (isMatch){
                  sbf.append("0")
                }else{
                  sbf.append("1")
                }

                //处理类似：蓝皖。。。。等车牌儿
//                if (isMatch) {
//                  val len = carlicense.length;
//                  carlicense = carlicense.substring(len - 7, len);
//                  words(6) = carlicense
//                }
//
//                var sortcarlicense: String = null
//
//                try {
//                  sortcarlicense = carlicense.substring(0, 1)
//                } catch {
//                  case e: Exception => e.printStackTrace();
//                }
//
//                val isInMap = Province.provinceMap.contains(sortcarlicense)
//
//                val isInSpecialMap = Province.specialplatnumber.contains(sortcarlicense)
//
//                //车牌是否有效【0：有效 1：无效 2：特殊车牌】
//                if (isMatch) {
//                  if (isInMap) {
//                    sbf.append("0")
//                  } else if (isInSpecialMap) {
//                    sbf.append("2")
//                  } else {
//                    sbf.append("1")
//                  }
//                } else {
//                  sbf.append("1")
//                }

                var entime = words(2).trim
                var exittime = words(5).trim
                //处理时间后面加00:00:00的格式
                if (Pattern.compile(eL1).matcher(entime).matches()){
                  entime = sdf.format(sdf.parse(entime))
                  words(2) = entime
                }
                if (Pattern.compile(eL1).matcher(exittime).matches()){
                  exittime = sdf.format(sdf.parse(exittime))
                  words(5) = exittime
                }

                //处理时间格式不匹配字段
                val p = Pattern.compile(eL)
                val m1 = p.matcher(entime)
                val m2 = p.matcher(exittime)
                val flag1 = m1.matches()
                val flag2 = m2.matches()
                if (!flag1) {
                  var exdate: Date = null
                  try {
                    exdate = sdf.parse(exittime)
                    val exseconds = exdate.getTime
                    val rkhm = exseconds - 60 * 60 * 1000
                    val enterdate = sdf.format(rkhm)
                    entime = enterdate
                    words(2) = entime
                  } catch {
                    case e: Exception => e.printStackTrace();
                  }

                } else if (!flag2) {
                  var endate: Date = null
                  try {
                    endate = sdf.parse(entime)
                    val enseconds = endate.getTime
                    val ckhm = enseconds + 60 * 60 * 1000
                    val exitdate = sdf.format(ckhm)
                    exittime = exitdate
                    words(5) = exittime
                  } catch {
                    case e: Exception => e.printStackTrace();
                  }

                }

                //是否超速 1未超速，0超速
                val mileage = words(9)
                val cartype = words(8)
                val overspeed: Boolean = isOverSpeed(entime, exittime, mileage,cartype)
                sbf.append(separator).append(if (overspeed == true) "1" else "0")

                //是否超载
                val weight = words(12)
                val limitweight = words(13)
                val overload = isOverLoad(weight, limitweight)
                sbf.append(separator).append(if (overload) "1" else "0")

                //自添加超限率计算

//                  val overrate = myoverrate(weight,limitweight)
//                  sbf.append(separator).append(overrate)


                //所在省编码、车牌分组（hebei，zhejiang）
                if (isMatch) {
                  val len = carlicense.length;
                  carlicense = carlicense.substring(len - 7, len);
                  val pName: String = carlicense.substring(0, 1);
                  val pro = Province.provinceMap2.get(pName);
                  var proStr = ""
                  if (pro.equals(None))
                    proStr = "none"
                  else
                    proStr = pro.get
                  sbf.append(separator).append(proStr)
                } else {
                  sbf.append(separator).append("none")
                }

                if (isMatch) {
                  val len = carlicense.length;
                  carlicense = carlicense.substring(len - 7, len);
                  val pName: String = carlicense.substring(0, 1);
                  val pro = Province.provinceMap.get(pName);
                  var proStr = ""
                  if (pro.equals(None))
                    proStr = "none"
                  else
                    proStr = pro.get
                  sbf.append(separator).append(proStr)
                } else {
                  sbf.append(separator).append("none")
                }

                //判断是否本省车 1为本省车 0为非本省车

                val localOrNot: Boolean = isLocal(province, carlicense)
                sbf.append(separator).append(if (localOrNot == true) "1" else "0")


                //是否本市车 1：本市 0：非本市
                //根据车牌号判断属于哪个市，根据入站口编号判断属于哪个市，看当前车是否属于入站口所在市。
                var cityNameByCar: String = "none"
                var provinceAndCity: String = null
                val zhixiashi = Set("上海", "天津", "北京", "重庆")

                val rkwlbh = words(0)
                val rkzbh = words(1)
                val ckwlbh = words(3)
                val ckzbh = words(4)
                val sfbm = words(21)

                val rkKey = sfbm + rkzbh + rkwlbh
                val ckkey = sfbm + ckzbh + ckwlbh


                try {
                  //车牌所在地
                  cityNameByCar = carLicenseInCity.getOrElse(carlicense.substring(0, 2), "none")
                } catch {
                  case e: Exception => e.printStackTrace();
                }

                val cityNameByEnter = sfzInCity.getOrElse(rkKey, new TollStation()).cityname

                val cityNameByExit = sfzInCity.getOrElse(ckkey, new TollStation()).cityname

                val enterProvice = sfzInCity.getOrElse(rkKey, new TollStation()).province

                val exitProvince = sfzInCity.getOrElse(ckkey, new TollStation()).province


                if (zhixiashi.contains(cityNameByCar)) {
                  provinceAndCity = cityNameByEnter
                } else {
                  provinceAndCity = enterProvice + cityNameByEnter
                }

                //                if (cityNameByEnter.equals("重庆")){
                //                  provinceAndCity = cityNameByEnter
                //                }else if (cityNameByEnter.equals("上海")){
                //                  provinceAndCity = cityNameByEnter
                //                }else{
                //                  provinceAndCity = enterProvice + cityNameByEnter
                //                }

                if (cityNameByCar.equals(provinceAndCity)) {
                  sbf.append(separator).append("1")
                } else {
                  sbf.append(separator).append("0")
                }


                //车牌所在市,
                sbf.append(separator).append(cityNameByCar)
                //进出口所在省市
                if (enterProvice.equals("none") && cityNameByEnter.equals("none")) {
                  sbf.append(separator).append("none")
                } else {
                  sbf.append(separator).append(enterProvice + cityNameByEnter)
                }
                if (exitProvince.equals("none") && cityNameByExit.equals("none")) {
                  sbf.append(separator).append("none")
                } else {
                  sbf.append(separator).append(exitProvince + cityNameByExit)
                }

                //判断是否夜间出行 是夜间出行为1 不是为0

                val ifNightOut: Boolean = isNightOut(words, entime, exittime)
                sbf.append(separator).append(if (ifNightOut == true) "1" else "0")


                //判断是否周末出行 是周末出行为1 不是为0

                val ifWeek: Boolean = isWeek(entime, exittime)
                sbf.append(separator).append(if (ifWeek == true) "1" else "0")



                //判断是否节假日出行 是节假日出行为1 不是为0

                val festivalOut: Boolean = isFestivalOut(entime, exittime)
                sbf.append(separator).append(if (festivalOut == true) "1" else "0")


                //判断是否为疲劳驾驶，是为1 否为0


                val tiredDrived: Boolean = isTiredDrived(entime, exittime, mileage)
                sbf.append(separator).append(if (tiredDrived == true) "1" else "0")

                //添加入口站名称和出口站名称

                val rkzmc = sfzInCity.getOrElse(rkKey, new TollStation()).sfzmc
                val ckzmc = sfzInCity.getOrElse(ckkey, new TollStation()).sfzmc

                sbf.append(separator).append(rkzmc).append(separator).append(ckzmc)

                sbf.append(separator).append(year + month + day)


                val lineStr = new StringBuilder
                for (word <- words) {
                  lineStr.append(word.trim).append(separator)
                }
                res = lineStr.toString + sbf.toString()
                res
              }

            }).repartition(5).saveAsTextFile(savePath)

          } catch {
            case e:Exception => e.printStackTrace();
          }
        }
      }
    }
  }

  /**
    * 是否超载
    *
    * @param weight      车货重量
    * @param limitweight 限重
    */
  def isOverLoad(weight: String, limitweight: String): Boolean = {
    //    if (StringUtils.isEmpty(weight) || StringUtils.isEmpty(limitweight))
    if ("NULL".equals(weight)|| "NULL".equals(limitweight))
      return false
    return weight.toDouble > limitweight.toDouble
  }

//  def myoverrate(weight: String, limitweight: String):String = {
//    var result:String = null
//    try{
//      var rate = (weight.toDouble - limitweight.toDouble) / limitweight.toDouble
//
//     result = f"$rate%1.4f".toString
//    }catch {
//      case e: Exception => {
//        e.printStackTrace()
//      }
//    }
//    result
//  }

  /**
    * 是否异地车 by wangmeng
    *
    * @param province 省份
    * @param carlicense 车牌号
    * @return Boolean
    */
  def isLocal(province:String,carlicense: String): Boolean = {
    var isLocal:Boolean = false
    try{
      val sortProvince = Province.haveDataProvinc.get(province)
      isLocal = carlicense.startsWith(sortProvince.get)
    }catch {
      case e:Exception => e.printStackTrace();
    }
    isLocal

  }

  /**判断是否夜间出行 by wangmeng 3.22 是为1，不是为0
    *
    * @param entime 入口时间
    * @param exittime 出口时间
    */
  def isNightOut(words:Array[String],entime:String,exittime:String): Boolean ={
    var isNight:Boolean = false
    try {
      //入口日期
      val rkdate = entime.split(" ")(0)
      //出口日期
      val ckdate = exittime.split(" ")(0)
      //规定夜间出行时间为夜间22时和凌晨4时之间
      val rktime = rkdate + " 22:00:00"
      //    val cktime = ckdate + " 04:00:00"
      //夜间22时转换为Date类型
      val startToDate = sdf.parse(rktime)
      //凌晨4时转换为Date类型
      //    val endToDate = sf.parse(cktime)
      //22时转换为毫秒数
      val startSecons = startToDate.getTime

      val difftime = 1000*60*60*6L
      //4时转换为毫秒数
      //    val endSeconds = endToDate.getTime
      val endSeconds = startSecons + difftime
      //入口时间转换为Date类型
      val rksj = sdf.parse(entime)
      //出口时间转换为Date类型
      val cksj = sdf.parse(exittime)
      //入口时间毫秒数（相对于1970 年1 月1 日午夜）
      val rksecons = rksj.getTime
      //出口时间毫秒数（（相对于1970 年1 月1 日午夜））
      val cksecons = cksj.getTime
      //判断 22时<入口时间<4时 或者 22时<出口时间<4时 规定为夜间出行
      isNight = (startSecons < rksecons && rksecons < endSeconds)|| (startSecons < cksecons && cksecons < endSeconds)
    }catch {
      case e:Exception => {
        e.printStackTrace()
      }
    }
    isNight
  }

  /**判断是否节假日出行 by wangmeng 3.22
    *
    * @param entime 入口时间
    * @param exittime 出口时间
    * @return
    */
  def isWeek(entime: String, exittime: String): Boolean = {
    var isWeek:Boolean = false
    try{

      //入口日期
      val rkdatestring = entime.split(" ")(0)

      //入口时间转换成Date类型
      val endate = sdf.parse(entime)

      //入口时间毫秒数
      val enSeconds = endate.getTime

      //将入口日期转换成Date格式
      val rkdate = df.parse(rkdatestring)

      //获取日历实例类
      val cal = Calendar.getInstance()
      //设置入口日历时间
      cal.setTime(rkdate)

      if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY){
        val sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val rknoontime = rkdatestring + " 12:00:00"

        val rkdateformat = sd.parse(rknoontime)

        val rkseconds = rkdateformat.getTime

        isWeek = enSeconds > rkseconds

      }else if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)
        ==Calendar.SUNDAY){
        isWeek = true
      }else{
        isWeek = false
      }
    }catch {
      case e:Exception => {
        e.printStackTrace()
      }
    }
    isWeek
  }

  /**是否节日出行 by wangmeng 3.22
    *
    * @param entime 入口时间
    * @param exittime 出口时间
    * @return
    */
  def isFestivalOut(entime: String, exittime: String): Boolean = {
    var isFesout:Boolean = false
    try{
      //入口日期
      val rkdatestring = entime.split(" ")(0)
      //出口日期
      val ckdatestring = exittime.split(" ")(0)

      isFesout = Festival.festivalMap.contains(rkdatestring)
    }catch {
      case e:Exception => {
        e.printStackTrace()
      }
    }
    isFesout
  }

  /**
    * 是否疲劳驾驶 by wangmeng 3.22
    *
    * @param entime 入口时间
    * @param exittime 出口时间
    * @param mileage 里程数
    * @return
    */
  def isTiredDrived(entime: String, exittime: String, mileage: String): Boolean = {
    var hour : Double = 0
    var lc : Double= 0
    var isTrie:Boolean = false
    try {
      //入口时间转换为Date类型
      val rksj = sdf.parse(entime)

      //出口时间转换为Date类型
      val cksj = sdf.parse(exittime)

      //入口时间毫秒数（相对于1970 年1 月1 日午夜）
      val rksecons = rksj.getTime

      //出口时间毫秒数（（相对于1970 年1 月1 日午夜））
      val cksecons = cksj.getTime

      //出口时间和入口时间差
      val difftime = cksecons - rksecons

      //时间差转换为小时
      hour = difftime / (1000 * 60 * 60)

      //将里程数转换为整型
      if ("NULL" == mileage) return false
      lc = mileage.toDouble
      isTrie = hour > 4.0 && lc > 400.0
    }catch{
      case e:Exception => {
        e.printStackTrace()
      }
    }
    isTrie
  }

  /**
    * 计算是否超速
    *
    * @param entime     入站时间
    * @param exittime   出站时间
    * @param mileageStr 里程
    * @return
    */
  def isOverSpeed(entime: String, exittime: String, mileageStr: String,cartype:String): Boolean = {
    var mileage: Double = 0
    var maxMileage: Double = 0
    try {
      val startTime: String = entime.substring(0, 19)
      val endTime: String = exittime.substring(0, 19)
      if (mileageStr == "NULL") return false
      mileage = mileageStr.toDouble
      val hours: Double = (DateUtils.parseDate(endTime, datePattern).getTime - DateUtils.parseDate(startTime, datePattern).getTime).toDouble / 3600000
      if (cartype.equals("0")){
        maxMileage = hours * 120
      } else {
        maxMileage = hours * 100
      }


    } catch {
      case e: Exception => {
        e.printStackTrace()
      }
    }
    return maxMileage < mileage
  }
}

object PCleanSourceDataV2 {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("cleanApplication")

    conf.set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
    conf.registerKryoClasses(Array(classOf[CleanSourceDataV2]))

    val sc = new SparkContext(conf)
    val cleanSourceData = new PCleanSourceDataV2;
    cleanSourceData.clean(sc)
    sc.stop();
  }
}
