package com.shujutang.highway.dataimport.toes;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * 删除所有index
 * <p>
 * Created by nancr on 16/1/12.
 */
public class DelAllIndex {
    public static void main(String[] args) {
        String rootUrl = "http://192.168.3.72:9600/";

        for (String province : Province.provices) {
            String url = rootUrl + province + "/";
//            ESRestClient client = new ESRestClient(url);
//            client.sendDelete();
//            client.closeClient();
            System.out.println("删除索引: " + province);
        }

    }

    private static class Province {
        static ImmutableList<String> provices;

        static {
            List<String> allprovice = Lists.newArrayList();
            allprovice.add("zhejiang");
            allprovice.add("anhui");
            allprovice.add("fujian");
            allprovice.add("guangdong");
            allprovice.add("hebei");
            allprovice.add("heilongjiang");
            allprovice.add("henan");
            allprovice.add("hubei");
            allprovice.add("hunan");
            allprovice.add("jiangsu");
            allprovice.add("jiangxi");
            allprovice.add("jilin");
            allprovice.add("liaoning");
            allprovice.add("ningxia");
            allprovice.add("shaanxi");
            allprovice.add("shanxi");
            allprovice.add("sichuan");
            allprovice.add("tianjin");
            allprovice.add("zhejiang");
            allprovice.add("guangxi");
            allprovice.add("chongqing");
            allprovice.add("shanghai");
            allprovice.add("shandong");
            allprovice.add("beijing");
            allprovice.add("neimenggu");
            allprovice.add("hainan");
            allprovice.add("guizhou");
            allprovice.add("yunnan");
            allprovice.add("xizang");
            allprovice.add("gansu");
            allprovice.add("qinghai");
            allprovice.add("xinjiang");

            provices = ImmutableList.copyOf(allprovice);
        }
    }
}
