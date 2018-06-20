package com.shujutang.highway.dataimport.hive2local

import java.io.FileWriter

import com.shujutang.highway.common.util.DateGenerate

/**
  * hive数据倒出到本地文件
  *修改后15年需要导出的字段 select RKWLBH,RKZBH,RKSJ, CKWLBH, CKZBH,CKSJ, CPH, CXDM, CZDM, LC, ZZS, ZXZZ, CHZZ, XZ, CXL, SFLSTD, MFLXDM, SFETC, OBU,ZFFS, SFKS, SFBM,SFMC from ods_sfmxb where
  * 用于交科院hadoop集群，
  * 此程序只生成导出命令，需要使用脚本执行命令（执行过程在交科院hadoop集群执行）。
  *
  * select t.RKWLBH as ennetcd, t.RKZBH as enstacd, m1.SFZMC as entname, t.RKSJ as entime,  t.CKWLBH as exitnetcd, t.CKZBH as exitstacd,m2.SFZMC as exitname, t.CKSJ as exittime, t.CKCDBH as exroadcd,  t.CPH as carlicense, t.CXDM as vhcmodel, t.CZDM as vhctype, t.LC as mileage, t.ZZS as axisnum, t.ZXZZ as axistype, t.PJZZ as pingjunzizhong,t.ZSXS as zhesuanxishu, t.CHZZ as weight, t.XZ as limitweight, t.CXL as limitrate,t.SFLSTD as greenCalflg, t.MFLXDM as opentype, t.LJBS as path, t.SFETC as etcflg, t.OBU as obu, t.ZFFS as paytype,t.SFKS as transproflg,t.RKZBZDM as entcd_bz,t.CKZBZDM as existcd_bz"
  * + " from ods_sfmxb t  left join DIM_ShouFeiZhan m1 on t.RKZBH = m1.SFZBH and m1.sf=? and m1.WLBH=t.RKWLBH left join DIM_ShouFeiZhan m2 on t.CKZBH = m2.SFZBH and m2.sf=? and m2.WLBH=t.CKWLBH"
  * + " where t.part_sf =? and t.part_nian =? and t.part_yue =? and  t.part_ri =? and  t.CKXS =?
  * Created by nancr on 2016/2/4.
  */
object Hive2Local_v2 {

  //TODO 开始和结束时间(包括开始结束时间，精确到天)
  val startTime = "20150101"
  val endTime = "20151231"

  val pattern = "yyyyMMdd"

  val rootPath = "/home/catsic/shujutang/data1/"
//  val rootPath = "/home/catsic/shujutang/data/"
  def main(args: Array[String]) {
    val fw = new FileWriter("D:/tmp/hivetolocalcmd.txt", true)

    //通过设定时间获取每天时间对象
    if (!DateGenerate.checkTime(startTime, endTime))
      throw new RuntimeException("开始时间必须小于结束时间")
    for (province <- PartProvince.haveDataProvinc.keySet) {

      //根据开始结束时间创建查询条件对象
      val argsList = DateGenerate.getAllDay(startTime, endTime, province, pattern)

      val it = argsList.iterator()
      while (it.hasNext)  {
        val onetime = it.next();
        val sortProvince = onetime.p.substring(2)
        val sql = new StringBuilder().
          append("select RKWLBH,RKZBH,RKSJ, CKWLBH, CKZBH,CKSJ, CPH, CXDM, CZDM, LC, ZZS, ZXZZ, CHZZ, XZ, CXL, SFLSTD, MFLXDM, SFETC, OBU,ZFFS, SFKS, SFBM,SFMC from ods_sfmxb where ").append("part_nian='").append(onetime.y).append("' and ").append("part_sf='").
          append(onetime.p).append("' and ").append("part_yue='").append(onetime.m).append("' and ").append("part_ri='").append(onetime.d).
          append("'; >> ").append(rootPath).append(sortProvince+"/").append(onetime.y).append("_").append(sortProvince).append("_").append(onetime.m).append("_").
          append(onetime.d).append(".txt\n")
//        val fw = new FileWriter("D:/tmp/hivetolocalcmd_"+sortProvince+".txt", true)
        println("指令：" + sql.toString())
        fw.write(sql.toString())

      }
    }
    fw.close()
    }
}



