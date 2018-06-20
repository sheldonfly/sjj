package com.shujutang.highway.common.domain

import scala.collection.mutable

/**
  * Created by nancr on 2016/2/18.
  */
object Province {

  //车牌号所在城市
  val carLicenseInCity = new mutable.HashMap[String, String]

  //收费站和城市的映射
  val sfzInCity = new mutable.HashMap[String, TollStation]

  //当前有数据的省份及对应的缩写
  var haveDataProvinc = new mutable.HashMap[String, String]()
  haveDataProvinc += ("hebei" -> "冀")
  haveDataProvinc += ("jiangxi" -> "赣")
  haveDataProvinc += ("liaoning" -> "辽")
  haveDataProvinc += ("shanghai" -> "沪")
  haveDataProvinc += ("guizhou" -> "贵")
  haveDataProvinc += ("guangxi" -> "桂")
  haveDataProvinc += ("henan" -> "豫")
  haveDataProvinc += ("ningxia" -> "宁")
  haveDataProvinc += ("tianjin" -> "津")
  haveDataProvinc += ("heilongjiang" -> "黑")
  haveDataProvinc += ("shanxi" -> "晋")
  haveDataProvinc += ("anhui" -> "皖")
  haveDataProvinc += ("guangdong" -> "粤")
  haveDataProvinc += ("sichuan" -> "川")
  haveDataProvinc += ("zhejiang" -> "浙")
  haveDataProvinc += ("shandong" -> "鲁")
  haveDataProvinc += ("chongqing" -> "渝")
  haveDataProvinc += ("jilin" -> "吉")
  haveDataProvinc += ("hunan" -> "湘")
  haveDataProvinc += ("shaanxi" -> "陕")
  haveDataProvinc += ("jiangsu" -> "苏")
  haveDataProvinc += ("hubei" -> "鄂")
  haveDataProvinc += ("fujian" -> "闽")
  haveDataProvinc += ("yunnan" -> "云")
  haveDataProvinc += ("gansu" -> "甘")
  haveDataProvinc += ("qinghai" -> "青")

  //所有省份对应的缩写
  var allProvince = new mutable.HashMap[String, String]()
  allProvince.put("zhejiang", "浙");
  allProvince.put("anhui", "皖");
  allProvince.put("fujian", "闽");
  allProvince.put("guangdong", "粤");
  allProvince.put("hebei", "冀");
  allProvince.put("heilongjiang", "黑");
  allProvince.put("henan", "豫");
  allProvince.put("hubei", "鄂");
  allProvince.put("hunan", "湘");
  allProvince.put("jiangsu", "苏");
  allProvince.put("jiangxi", "赣");
  allProvince.put("jilin", "吉");
  allProvince.put("liaoning", "辽");
  allProvince.put("ningxia", "宁");
  allProvince.put("shaanxi", "陕");
  allProvince.put("shanxi", "晋");
  allProvince.put("sichuan", "川");
  allProvince.put("tianjin", "津");
  allProvince.put("guangxi", "桂");
  allProvince.put("chongqing", "渝");
  allProvince.put("shanghai", "沪");
  allProvince.put("shandong", "鲁");
  allProvince.put("beijing", "京");
  allProvince.put("neimenggu", "蒙");
  allProvince.put("hainan", "琼");
  allProvince.put("guizhou", "贵");
  allProvince.put("yunnan", "云");
  allProvince.put("xizang", "藏");
  allProvince.put("gansu", "甘");
  allProvince.put("qinghai", "青");
  allProvince.put("xinjiang", "新");


  var provinceMap = new mutable.HashMap[String, String]
    provinceMap.put("浙", "zhejiang");
    provinceMap.put("皖", "anhui");
    provinceMap.put("闽", "fujian");
    provinceMap.put("粤", "guangdong");
    provinceMap.put("冀", "hebei");
    provinceMap.put("黑", "heilongjiang");
    provinceMap.put("豫", "henan");
    provinceMap.put("鄂", "hubei");
    provinceMap.put("湘", "hunan");
    provinceMap.put("苏", "jiangsu");
    provinceMap.put("赣", "jiangxi");
    provinceMap.put("吉", "jilin");
    provinceMap.put("辽", "liaoning");
    provinceMap.put("宁", "ningxia");
    provinceMap.put("陕", "shaanxi");
    provinceMap.put("晋", "shanxi");
    provinceMap.put("川", "sichuan");
    provinceMap.put("津", "tianjin");
    provinceMap.put("桂", "guangxi");
    provinceMap.put("渝", "chongqing");
    provinceMap.put("沪", "shanghai");
    provinceMap.put("鲁", "shandong");
    provinceMap.put("京", "beijing");
    provinceMap.put("蒙", "neimenggu");
    provinceMap.put("琼", "hainan");
    provinceMap.put("贵", "guizhou");
    provinceMap.put("云", "yunnan");
    provinceMap.put("藏", "xizang");
    provinceMap.put("甘", "gansu");
    provinceMap.put("青", "qinghai");
    provinceMap.put("新", "xinjiang");

  var provinceMap2 = new mutable.HashMap[String, String]
  provinceMap2.put("浙", "浙江");
  provinceMap2.put("皖", "安徽");
  provinceMap2.put("闽", "福建");
  provinceMap2.put("粤", "广东");
  provinceMap2.put("冀", "河北");
  provinceMap2.put("黑", "黑龙江");
  provinceMap2.put("豫", "河南");
  provinceMap2.put("鄂", "湖北");
  provinceMap2.put("湘", "湖南");
  provinceMap2.put("苏", "江苏");
  provinceMap2.put("赣", "江西");
  provinceMap2.put("吉", "吉林");
  provinceMap2.put("辽", "辽宁");
  provinceMap2.put("宁", "宁夏");
  provinceMap2.put("陕", "陕西");
  provinceMap2.put("晋", "山西");
  provinceMap2.put("川", "四川");
  provinceMap2.put("津", "天津");
  provinceMap2.put("桂", "广西");
  provinceMap2.put("渝", "重庆");
  provinceMap2.put("沪", "上海");
  provinceMap2.put("鲁", "山东");
  provinceMap2.put("京", "北京");
  provinceMap2.put("蒙", "内蒙古");
  provinceMap2.put("琼", "海南");
  provinceMap2.put("贵", "贵州");
  provinceMap2.put("云", "云南");
  provinceMap2.put("藏", "西藏");
  provinceMap2.put("甘", "甘肃");
  provinceMap2.put("青", "青海");
  provinceMap2.put("新", "新疆");

  var name2Number = new mutable.HashMap[String, String]
  name2Number.put("浙江", "zhejiang");
  name2Number.put("安徽", "anhui");
  name2Number.put("福建", "fujian");
  name2Number.put("广东", "guangdong");
  name2Number.put("河北", "hebei");
  name2Number.put("黑龙江", "heilongjiang");
  name2Number.put("河南", "henan");
  name2Number.put("湖北", "hubei");
  name2Number.put("湖南", "hunan");
  name2Number.put("江苏", "jiangsu");
  name2Number.put("江西", "jiangxi");
  name2Number.put("吉林", "jilin");
  name2Number.put("辽宁", "liaoning");
  name2Number.put("宁夏", "ningxia");
  name2Number.put("陕西", "shaanxi");
  name2Number.put("山西", "shanxi");
  name2Number.put("四川", "sichuan");
  name2Number.put("天津", "tianjin");
  name2Number.put("广西", "guangxi");
  name2Number.put("重庆", "chongqing");
  name2Number.put("上海", "shanghai");
  name2Number.put("山东", "shandong");
  name2Number.put("北京", "beijing");
  name2Number.put("内蒙古", "neimenggu");
  name2Number.put("海南", "hainan");
  name2Number.put("贵州", "guizhou");
  name2Number.put("云南", "yunnan");
  name2Number.put("西藏", "xizang");
  name2Number.put("甘肃", "gansu");
  name2Number.put("青海", "qinghai");
  name2Number.put("新疆", "xinjiang");


  val specialplatnumber = new mutable.HashMap[String, String]

  specialplatnumber.put("兰", "lan");
  specialplatnumber.put("海", "hai");
  specialplatnumber.put("警", "jing");
  specialplatnumber.put("军", "jun");
  specialplatnumber.put("北", "bei");
  specialplatnumber.put("南", "nan");
  specialplatnumber.put("广", "guang");
  specialplatnumber.put("沈", "shen");
  specialplatnumber.put("成", "cheng");
  specialplatnumber.put("济", "ji");
  specialplatnumber.put("空", "kong");
  specialplatnumber.put("边", "bian");
  specialplatnumber.put("金", "jin");
  specialplatnumber.put("森", "sen");
  specialplatnumber.put("通", "tong");
  specialplatnumber.put("电", "dian");
  specialplatnumber.put("甲", "jia");
  specialplatnumber.put("乙", "yi");
  specialplatnumber.put("丙", "bing");
  specialplatnumber.put("己", "ji");
  specialplatnumber.put("庚", "geng");
  specialplatnumber.put("辛", "xin");
  specialplatnumber.put("壬", "ren");
  specialplatnumber.put("寅", "yin");
  specialplatnumber.put("辰", "chen");
  specialplatnumber.put("戍", "shu");
  specialplatnumber.put("午", "wu");
  specialplatnumber.put("未", "wei");
  specialplatnumber.put("申", "shen");
}
