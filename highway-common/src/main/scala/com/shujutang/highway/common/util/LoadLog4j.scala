package com.shujutang.highway.common.util

import java.io.{IOException, InputStream}
import java.util.Properties

import org.apache.log4j.PropertyConfigurator
import org.slf4j.LoggerFactory

/**
  * Created by nancr on 2016/9/8.
  */
object LoadLog4j {
  val log = LoggerFactory.getLogger(LoadLog4j.getClass);
  def load(configFilePath: String) {
    try {
      val is: InputStream = LoadLog4j.getClass.getClassLoader.getResourceAsStream(configFilePath)
      val pro: Properties = new Properties
      pro.load(is)
      if (is != null) {
        is.close
      }
      PropertyConfigurator.configure(pro)
      log.info("加载log4j配置[" + configFilePath + "]成功")
    }
    catch {
      case e: IOException => {
        log.error("装载log4j配置失败,失败原因：", e)
      }
    }
  }
}
