package com.shujutang.highway.dataimport.toes;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractExecutionThreadService;

/**
 * 创建索引及映射(只需调用一次)
 * 每个索引:主分片5.
 * 按省分索引，按月分type。 5主分片，每分片一段
 *
 * Created by nancr on 15/11/17.
 */
public class CreateIndex {

//    private static class DefaultCreateIndex extends AbstractExecutionThreadService{
//        Map<String, ESRestClient> clientMap = Maps.newConcurrentMap();
//        ImmutableList<String> list = ImmutableList.of("zhejiang", "anhui", "fujian", "guangdong", "hebei",
//                "heilongjiang", "henan", "hubei", "hunan", "jiangsu", "jiangxi", "jilin", "liaoning", "ningxia",
//                "shaanxi", "shanxi", "sichuan", "tianjin", "guangxi", "chongqing", "shanghai", "shandong",
//                "beijing", "neimenggu", "hainan", "guizhou", "yunnan", "xizang", "gansu", "qinghai", "xinjiang",
//                "null");
//        StringBuilder query = new StringBuilder();
//        String url = "http://192.168.3.72:9600/";
//        @Override
//        protected void run() throws Exception {
//
//            //按月创建索引
//            for (String province : list){
//                System.out.println("创建索引:" + province);
//                ESRestClient client = clientMap.get(province);
//                client.sendDelete();
//                client.createIndex(query.toString());
//                client.closeClient();
//            }
//
//        }
//
//        @Override
//        protected void startUp() throws Exception {
//            System.out.println("服务启动...");
//            for (String province : list){
//                clientMap.put(province, new ESRestClient(url + province));
//            }
//
//            query.append(" {");
//            query.append("   \"settings\": {");
//            query.append("       \"number_of_shards\": 4");
//            query.append("    },");
//            query.append("    \"mappings\" : {");
//            query.append("       \"_default_\" : {");
//            query.append("          \"dynamic\" : \"strict\",");
//            query.append("           \"properties\": {");
//            query.append("             \"vhctype\": {");
//            query.append("                 \"type\": \"string\",");
//            query.append("                 \"index\": \"not_analyzed\"");
//            query.append("             },");
//            query.append("             \"mileage\": {");
//            query.append("                 \"type\": \"double\"");
//            query.append("              },");
//            query.append("             \"entname\": {");
//            query.append("                 \"type\": \"string\",");
//            query.append("                 \"analyzer\": \"ik_smart\"");
//            query.append("              },");
//            query.append("             \"weight\": {");
//            query.append("                 \"type\": \"double\"");
//            query.append("              },");
//            query.append("             \"transproflg\": {");
//            query.append("                 \"type\": \"string\",");
//            query.append("                 \"index\": \"not_analyzed\"");
//            query.append("              },");
//            query.append("             \"exittime\":{");
//            query.append("               \"type\": \"date\",");
//            query.append("               \"format\":\"yyyy-MM-dd HH:mm:ss\"");
//            query.append("             },");
//            query.append("             \"exitstacd\": {");
//            query.append("                 \"type\": \"string\",");
//            query.append("                 \"index\": \"not_analyzed\"");
//            query.append("              },");
//            query.append("             \"enstacd\": {");
//            query.append("                 \"type\": \"string\",");
//            query.append("                 \"index\": \"not_analyzed\"");
//            query.append("              },");
//            query.append("             \"carlicense\": {");
//            query.append("                 \"type\": \"string\",");
//            query.append("                 \"index\": \"not_analyzed\"");
//            query.append("              },");
//            query.append("             \"vhcmodel\": {");
//            query.append("                 \"type\": \"string\",");
//            query.append("                 \"index\": \"not_analyzed\"");
//            query.append("              },");
//            query.append("             \"limitweight\": {");
//            query.append("                 \"type\": \"double\"");
//            query.append("             },");
//            query.append("             \"entime\":{");
//            query.append("               \"type\": \"date\",");
//            query.append("               \"format\":\"yyyy-MM-dd HH:mm:ss\"");
//            query.append("             },");
//            query.append("              \"month\":{");
//            query.append("               \"type\": \"date\",");
//            query.append("               \"format\":\"yyyy-MM-dd\"");
//            query.append("             },");
//            query.append("             \"exitname\": {");
//            query.append("                 \"type\": \"string\",");
//            query.append("                 \"analyzer\": \"ik_smart\"");
//            query.append("             }, ");
//            query.append("             \"limitrate\": {");
//            query.append("                 \"type\": \"double\"");
//            query.append("             },");
//            query.append("             \"overspeed\":{");
//            query.append("                 \"type\":\"integer\"");
//            query.append("             },");
//            query.append("              \"overload\":{");
//            query.append("                 \"type\":\"integer\"");
//            query.append("             }");
//            query.append("           }");
//            query.append("       }");
//            query.append("    }");
//            query.append(" }");
//
//
//        }
//
//        @Override
//        protected void shutDown() throws Exception {
//            //关闭连接和线程池
//            System.out.println("服务结束!");
//        }
//    }
//
//    public static void main(String[] args) {
//        DefaultCreateIndex service = new DefaultCreateIndex();
//        service.startAsync().awaitRunning();
//        String stat = service.state().toString();
//        System.out.println("状态:" + stat);
//
//    }
}
