package com.shujutang.highway.dataimport.clean.v2

/**
  * Created by wangmeng on 2016/9/23.
  */
object RegexUtil {

  val RegEx = "[京][A-Z][0-9A-HJ-NP-Z]{5}|[京][A-Z][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[津][A-Z][0-9A-HJ-NP-Z]{5}|[津][A-Z][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]" +
    "|[晋][OABCDEFHJKLM][0-9A-HJ-NP-Z]{5}|[晋][OABCDEFHJKLM][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[冀][OABCDEFGHJRT][0-9A-HJ-NP-Z]{5}|[冀][OABCDEFGHJRT]" +
    "[0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[蒙][OABCDEFGHJKLM][0-9A-HJ-NP-Z]{5}|[蒙][OABCDEFGHJKLM][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[辽][OABCDEFGHJKLMNP]" +
    "[0-9A-HJ-NP-Z]{5}|[辽][OABCDEFGHJKLMNP][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[吉][OABCDEFGHJK][0-9A-HJ-NP-Z]{5}|[吉][OABCDEFGHJK][0-9A-HJ-NP-Z]{4}" +
    "[港澳领警学试超临]|[黑][OALBCDEFGHJKMNPR][0-9A-HJ-NP-Z]{5}|[黑][0ALBCDEFGHJKMNPR][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[沪][A-Z][0-9A-HJ-NP-Z]{5}|[沪]" +
    "[A-Z][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[苏][OABCDEFGHJKLMN][0-9A-HJ-NP-Z]{5}|[苏][OABCDEFGHJKLMN][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[浙][OABCDEFGHJKL]" +
    "[0-9A-HJ-NP-Z]{5}|[浙][OABCDEFGHJKL][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[皖][OABCDEFGHJKLMNPQRS][0-9A-HJ-NP-Z]{5}|[皖][OABCDEFGHJKLMNPQRS][0-9A-HJ-NP-Z]" +
    "{4}[港澳领警学试超临]|[闽][OAKBCDEFGHJ][0-9A-HJ-NP-Z]{5}|[闽][OAKBCDEFGHJ][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[赣][OAMBCDEFGHJKL][0-9A-HJ-NP-Z]{5}|[赣]" +
    "[OAMBCDEFGHJKL][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[鲁][OABUCDEFYGVHJKLMNPQRS][0-9A-HJ-NP-Z]{5}|[鲁][OABUCDEFYGVHJKLMNPQRS][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]" +
    "|[豫][OABCDEFGHJKLMNPQRSU][0-9A-HJ-NP-Z]{5}|[豫][OABCDEFGHJKLMNPQRSU][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[鄂][OABCDEFGHJKLMNPQRS][0-9A-HJ-NP-Z]{5}|[鄂]" +
    "[OABCDEFGHJKLMNPQRS][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[湘][OASBCDEFGHJKLMNU][0-9A-HJ-NP-Z]{5}|[湘][OASBCDEFGHJKLMNU][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]" +
    "|[粤][OABCDEXYFGHJKLMNPQRSTUVWZ][0-9A-HJ-NP-Z]{5}|[粤][OABCDEXYFGHJKLMNPQRSTUVWZ][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[桂][OABCHDEFGJKLMNPR][0-9A-HJ-NP-Z]" +
    "{5}|[桂][OABCHDEFGJKLMNPR][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[琼][OABCDE][0-9A-HJ-NP-Z]{5}|[琼][OABCDE][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[渝][A-Z]" +
    "[0-9A-HJ-NP-Z]{5}|[渝][A-Z][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[川][OABCDEFHJKLMQRSTUVWXYZ][0-9A-HJ-NP-Z]{5}|[川][OABCDEFHJKLMQRSTUVWXYZ][0-9A-HJ-NP-Z]" +
    "{4}[港澳领警学试超临]|[贵][OABCDEFGHJ][0-9A-HJ-NP-Z]{5}|[贵][OABCDEFGHJ][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[云][OACDEFGHJKLMNPQRS][0-9A-HJ-NP-Z]{5}|[云]" +
    "[OACDEFGHJKLMNPQRS][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[藏][OABCDEFGHJ][0-9A-HJ-NP-Z]{5}|[藏][OABCDEFGHJ][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[陕][OABCDEFGHJKV]" +
    "[0-9A-HJ-NP-Z]{5}|[陕][OABCDEFGHJKV][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[甘][OABCDEFGHJKLMNP][0-9A-HJ-NP-Z]{5}|[甘][OABCDEFGHJKLMNP][0-9A-HJ-NP-Z]{4}" +
    "[港澳领警学试超临]|[青][OABCDEFGH][0-9A-HJ-NP-Z]{5}|[青][OABCDEFGH][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[宁][OABCDE][0-9A-HJ-NP-Z]{5}|[宁][OABCDE][0-9A-HJ-NP-Z]" +
    "{4}[港澳领警学试超临]|[新][OABCEDFGHJKLMNPQR][0-9A-HJ-NP-Z]{5}|[新][OABCEDFGHJKLMNPQR][0-9A-HJ-NP-Z]{4}[港澳领警学试超临]|[使][0-9A-HJ-NP-Z]{6}"
}
