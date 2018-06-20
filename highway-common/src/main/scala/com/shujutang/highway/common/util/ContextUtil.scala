package com.shujutang.highway.common.util

import org.apache.commons.lang.time.{DateFormatUtils, DateUtils}
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}
import java.util.Date

import org.apache.spark.sql.SQLContext

/**
  * 创建hivecontext实例环境
  *
 * Created by nancr on 2016/5/16.
 */
trait ContextUtil{
  @transient
  val sc = getSparkContext("highway_product")
  @transient
  val ssc = new SQLContext(sc)
  @transient
  val hc = getHiveContext0(sc)

  /**
    * 创建sparkcontext
    * @return
    */
  def getSparkContext(appName : String, masterUrl : String = "spark://hadoop160:7077"): SparkContext ={
    @transient
    val conf = new SparkConf().setAppName(appName)
//      .setMaster(masterUrl)
    @transient
    val sc = new SparkContext(conf)
    sc
  }

  /**
    * 创建hivecontext
    * @return
    */
  def getHiveContext(appName : String, masterUrl : String = "spark://hadoop160:7077"): HiveContext ={
    val sc = getSparkContext(appName, masterUrl)
    val hc = new HiveContext(sc)
    hc
  }

  def getHiveContext0(sc: SparkContext, masterUrl : String = "spark://hadoop160:7077"): HiveContext ={
    val hc = new HiveContext(sc)
    hc
  }



}
