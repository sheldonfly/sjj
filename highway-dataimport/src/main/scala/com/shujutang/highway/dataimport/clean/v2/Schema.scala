package com.shujutang.highway.dataimport.clean.v2

import org.apache.spark.sql.types.{DoubleType, StringType, StructField, StructType}

/**
  * Created by nancr on 2016/4/20.
  */
object Schema {

  def getSchema(): StructType ={
    // The schema is encoded in a string
    val schemaString = "rkwlbh,rkzbh,entime,ckwlbh,ckzbh,exittime,carlicense,vhcmodel,vhctype,mileage,axisnum," +
      "axistype,totalweight,limitweight,limitrate,greencalfcode,freechargecode,etcflg,obu,paytype,transproflg,procode," +
      "proname,isvalid,overspeed,overload,carprovince,isprovince,iscity,carcity,entercity,exitcity,isnightOut,isweekout," +
      "isfestivalout,istireddrived,enname,extname"

    val doubleField = List("mileage", "totalweight", "limitweight", "limitrate")
    // Import Spark SQL data types
    import org.apache.spark.sql.types.{StructType,StructField,StringType};

    // Generate the schema based on the string of schema
    val schema =
      StructType(
        schemaString.split(",").map(fieldName => {
          if (doubleField.contains(fieldName)){
            StructField(fieldName, DoubleType, true)
          }else{
            StructField(fieldName, StringType, true)
          }
         }))
    schema
  }

}
