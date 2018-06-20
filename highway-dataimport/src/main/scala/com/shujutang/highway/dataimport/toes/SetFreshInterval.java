package com.shujutang.highway.dataimport.toes;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractExecutionThreadService;

/**
 * 设置刷新间隔
 *大批量插入数据是设置为-1不刷新，数据插入完毕后设置为2.
 *
 * Created by nancr on 15/11/19.
 */
public class SetFreshInterval {


    public static void main(String[] args) {
        FreshInterval service = new FreshInterval();
        service.startAsync().awaitRunning();
    }

    private static class FreshInterval extends AbstractExecutionThreadService{
        String url = "http://192.168.203.142:9200/";
//        private Map<String, ESRestClient> clientMap = Maps.newConcurrentMap();

        //index列表
        private ImmutableList<String> list = null;
        //参数
        private StringBuilder put = new StringBuilder();

        @Override
        protected void startUp() throws Exception {
            System.out.println("服务启动...");

            list = ImmutableList.of("zhejiang", "anhui", "fujian", "guangdong", "hebei",
                    "heilongjiang", "henan", "hubei", "hunan", "jiangsu", "jiangxi", "jilin", "liaoning", "ningxia",
                    "shaanxi", "shanxi", "sichuan", "tianjin", "guangxi", "chongqing", "shanghai", "shandong",
                    "beijing", "neimenggu", "hainan", "guizhou", "yunnan", "xizang", "gansu", "qinghai", "xinjiang",
                    "null");

            for (String province : list){
//                clientMap.put(province, new ESRestClient(url + province + "/_settings"));
            }

//            put.append("{ \"refresh_interval\": -1 }");
            put.append("{ \"refresh_interval\": \"2s\" }");
        }

        @Override
        protected void run() throws Exception {
            for (String province : list){
//                clientMap.get(province).sendPut(put.toString());
            }
        }

        @Override
        protected void shutDown() throws Exception {
            System.out.println("service执行完成!");
        }
    }

}
