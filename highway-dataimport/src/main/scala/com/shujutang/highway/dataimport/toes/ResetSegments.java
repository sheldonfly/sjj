package com.shujutang.highway.dataimport.toes;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractExecutionThreadService;

/**
 * 重置各个索引每个分片段数为1
 * 提升检索速度
 *
 * curl -XGET "http://192.168.203.142:9200/jilin/_segments"
 *
 * Created by nancr on 15/11/23.
 */
public class ResetSegments {

    public static void main(String[] args) {
        Segment2One service = new Segment2One();
        service.startAsync().awaitRunning();
    }

    private static class Segment2One extends AbstractExecutionThreadService{
        String url = "http://192.168.203.142:9200/";
//        private Map<String, ESRestClient> clientMap = Maps.newConcurrentMap();

        //index列表
        private ImmutableList<String> list = null;

        @Override
        protected void startUp() throws Exception {
            System.out.println("服务启动...");

            list = ImmutableList.of("zhejiang", "anhui", "fujian", "guangdong", "hebei",
                    "heilongjiang", "henan", "hubei", "hunan", "jiangsu", "jiangxi", "jilin", "liaoning", "ningxia",
                    "shaanxi", "shanxi", "sichuan", "tianjin", "guangxi", "chongqing", "shanghai", "shandong",
                    "beijing", "neimenggu", "hainan", "guizhou", "yunnan", "xizang", "gansu", "qinghai", "xinjiang",
                    "null");

//            for (String province : list){
//                clientMap.put(province, new ESRestClient(url + province + "/_optimize?max_num_segments=1"));
//            }

        }

        @Override
        protected void run() throws Exception {
//            //按月创建索引
//            for (String province : list){
//                System.out.println("重置分段数 :" + province);
//                ESRestClient client = new ESRestClient(url + province + "/_optimize?max_num_segments=1");
//                client.sendPost();
//                client.closeClient();
////                clientMap.get(province).sendPost();
//                System.out.println(province + " 完成");
//            }
        }

        @Override
        protected void shutDown() throws Exception {
            System.out.println("所有操作完成");
        }
    }
}
