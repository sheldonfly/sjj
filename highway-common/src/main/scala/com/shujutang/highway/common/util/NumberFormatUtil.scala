package com.shujutang.highway.common.util

import java.text.NumberFormat

/**
  * Created by nancr on 2016/12/13.
  */
object NumberFormatUtil {

  /**
    * 得到数值格式化对象
    * @param decimalPlace 小数点位数
    * @return
    */
  def getNumberFormat(decimalPlace: Int) : NumberFormat = {
    val numberFormat = NumberFormat.getInstance();
    numberFormat.setMaximumFractionDigits(decimalPlace);
    numberFormat
  }
}
