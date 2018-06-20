package com.shujutang.highway.dataimport.hive2local

import java.io.FileWriter
import java.net.URI
import java.sql.{Statement, DriverManager, Connection}

import com.shujutang.highway.common.domain.Province
import com.shujutang.highway.common.util.DateGenerate
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem

/**
  * hive数据倒出到本地文件
  *
  * hadoop jar highway-dataimport-hive2local.jar com.shujutang.highway.dataimport.hive2local.Hive2Local_v3
  *
  * 用于交科院hadoop集群，
  * 此程序只生成导出命令，需要使用脚本执行命令（执行过程在交科院hadoop集群执行）。
  *
  * select t.RKWLBH as ennetcd, t.RKZBH as enstacd, m1.SFZMC as entname, t.RKSJ as entime,  t.CKWLBH as exitnetcd, t.CKZBH as exitstacd,m2.SFZMC as exitname, t.CKSJ as exittime, t.CKCDBH as exroadcd,  t.CPH as carlicense, t.CXDM as vhcmodel, t.CZDM as vhctype, t.LC as mileage, t.ZZS as axisnum, t.ZXZZ as axistype, t.PJZZ as pingjunzizhong,t.ZSXS as zhesuanxishu, t.CHZZ as weight, t.XZ as limitweight, t.CXL as limitrate,t.SFLSTD as greenCalflg, t.MFLXDM as opentype, t.LJBS as path, t.SFETC as etcflg, t.OBU as obu, t.ZFFS as paytype,t.SFKS as transproflg,t.RKZBZDM as entcd_bz,t.CKZBZDM as existcd_bz"
  * + " from ods_sfmxb t  left join DIM_ShouFeiZhan m1 on t.RKZBH = m1.SFZBH and m1.sf=? and m1.WLBH=t.RKWLBH left join DIM_ShouFeiZhan m2 on t.CKZBH = m2.SFZBH and m2.sf=? and m2.WLBH=t.CKWLBH"
  * + " where t.part_sf =? and t.part_nian =? and t.part_yue =? and  t.part_ri =? and  t.CKXS =?
  * Created by nancr on 2016/2/4.
  */
@deprecated
object Hive2Local_v3 {

  //TODO 开始和结束时间(包括开始结束时间，精确到天)
  val startTime = "20150101"
  val endTime = "20150231"

  val pattern = "yyyyMMdd"

  //本地文件根目录
  val localRootPath = "/data/saprkhome/file/hive2local/"
  //hdfs文件根目录
  val hdfsRootPath = "/user/hive/warehouse/highway/"

  def main(args: Array[String]) {
    val fileSystem = FileSystem.get(new URI("hdfs://hadoop1:9000"), new Configuration, "spark")

    //通过设定时间获取每天时间对象
    if (!DateGenerate.checkTime(startTime, endTime))
      throw new RuntimeException("开始时间必须小于结束时间")

    //创建hive连接
    Class.forName("org.apache.hive.jdbc.HiveDriver")
    val conn: Connection = DriverManager.getConnection("jdbc:hive2://192.168.3.72:10000/default", "spark", "")
    val stmt: Statement = conn.createStatement

    for (province <- Province.haveDataProvinc.keySet) {
      //根据开始结束时间创建查询条件对象
      val argsList = DateGenerate.getAllDay(startTime, endTime, province, pattern)

      val it = argsList.iterator()
      while (it.hasNext) {
        val onetime = it.next();
        val year = onetime.y
        val month = onetime.m
        val day = onetime.d

        val filePath1 = hdfsRootPath + "year=" + year + "/province=" + province + "/month=" + month + "/day=" + day

        val isExit = fileSystem.exists(new org.apache.hadoop.fs.Path(filePath1))
        if (isExit) {
          val localPath = year + "_" + province + "_" + month + "_" + day
          val sql = new StringBuilder().append("insert overwrite local directory '")
            .append(localRootPath + localPath)
            .append("' select ennetcd, enstacd, entname, entime, exitnetcd, exitstacd, exitname, exittime, " +
              "exroadcd, carlicense, vhcmodel, vhctype, mileage, axisnum, axistype, averageweight, zhesuanxishu, " +
              "totalweight, limitweight, limitrate, greencalfcode,  freechargecode, pathflag, etcflg, obu, paytype, " +
              "transproflg, entcd_bz, existcd_bz, isvalid, overspeed, overload, progroup from highway where ")
            .append("year='").append(year).append("' and ").append("province='")
            .append(province).append("' and ").append("month='").append(month).append("' and ")
            .append("day='").append(day).append("'")

          val exeRes = stmt.execute(sql.toString)
          println("###########################数据导出结果：" + exeRes)
        }
      }
    }
  }
}



