package com.shujutang.highway.dataimport.partition

import java.io.FileWriter
import java.util.Date

import com.shujutang.highway.common.domain.Province
import com.shujutang.highway.common.util.DateGenerate
import org.apache.commons.lang.time.DateFormatUtils

/**
  * 创建分区表后，生成hive分区命令
  *
  * 指定年和省，将分区指令输出到文件
  *
  * 执行：
  * Created by nancr on 2016/2/3.
  */
object AddHiveMetaData_v3 {
	val startTime = "20161001"
	val endTime = "20161031"
	var datePattern = Array("yyyy-MM-dd HH:mm:ss")
	val pattern = "yyyyMMdd"

	//数据存储根目录
	val rootPath = "/user/hive/warehouse/highway/"

	//cmd输出目录
	val cmdFile = "D:/tmp/hivepartition.txt"

	def main(args: Array[String]) {

		println("开始执行时间:" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"))
		val fileWriter = new FileWriter(cmdFile, true)

		//循环所有省数据
		for (province <- Province.haveDataProvinc.keySet) {
			val allDays = DateGenerate.getAllDay(startTime, endTime, province, pattern)
			//处理当前省每天的数据
			val it = allDays.iterator
			while (it.hasNext) {
				val oneDay = it.next
				val year = oneDay.y
				val month = oneDay.m
				val day = oneDay.d

				//分区路径
				val cmd = new StringBuilder("alter table highway add partition (").
					append("year='").append(year).append("', ").append("province='").append(province).append("',").
					append("month='").append(month).append("',").append("day='").append(day).append("')").
					append(" location ").append("'" + rootPath).append("year=").append(year).append("/province=").append(province).
					append("/month=").append(month).
					append("/day=").append(day).append("';").append("\n")
				println("cmd=" + cmd.toString())
				//将信息输出到文件
				fileWriter.write(cmd.toString())
			}
		}
		fileWriter.close
		println("执行结束时间:" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"))
	}
}