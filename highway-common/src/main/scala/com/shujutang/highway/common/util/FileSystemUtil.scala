package com.shujutang.highway.common.util

import java.net.URI

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

/**
  * 文件系统
  * hdfs 以及 本地文件系统
  * Created by nancr on 2016/4/22.
  */
object FileSystemUtil {
  @transient
  val conf = new Configuration
  val hdfs = FileSystem.get(URI.create("/"), conf, "spark")

  def isExistHdfs(path: String): Boolean = {
    val isExit = hdfs.exists(new Path(path))
    isExit
  }

  def delHdfs(path: String): Boolean ={
    hdfs.delete(new Path(path), true)
  }

}
