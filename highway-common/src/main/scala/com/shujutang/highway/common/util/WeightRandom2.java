package com.shujutang.highway.common.util;

import java.util.*;

/**
 * Created by nancr on 2015/10/12.
 */
public class WeightRandom2 {
    static List<WeightCategory2> categorys = new ArrayList<>();
    private static Random random = new Random();

    private static Map<String, Integer> resMap = new HashMap<>();

    public static void initData() {
        WeightCategory2 wc1 = new WeightCategory2("A", 6);
        WeightCategory2 wc2 = new WeightCategory2("B", 2);
        WeightCategory2 wc3 = new WeightCategory2("C", 2);
        categorys.add(wc1);
        categorys.add(wc2);
        categorys.add(wc3);
    }

    public static void main(String[] args) {
        initData();
        //循环 1000
        for (int i = 0; i < 100; i++) {
            Integer weightSum = 0;
            for (WeightCategory2 wc : categorys) {
                weightSum += wc.getWeight();
            }

            if (weightSum <= 0) {
                System.err.println("Error: weightSum=" + weightSum.toString());
                return;
            }

            Integer m = 0;
            Integer n = random.nextInt(weightSum); // n in [0, weightSum)
            for (WeightCategory2 wc : categorys) {
                if (m <= n && n < m + wc.getWeight()) {
                    String categry = wc.getCategory();
                    if (resMap.keySet().contains(categry)) {
                        int count = resMap.get(categry);
                        count = ++count;
                        resMap.put(categry, count);
                    } else {
                        resMap.put(categry, 1);
                    }
                    System.out.println("This Random Category is "+ wc.getCategory());
                    break;
                }
                m += wc.getWeight();
            }
        }
        System.out.println(resMap);
    }

}

class WeightCategory2 {
    private String category;
    private Integer weight;


    public WeightCategory2() {
        super();
    }

    public WeightCategory2(String category, Integer weight) {
        super();
        this.setCategory(category);
        this.setWeight(weight);
    }


    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}