package com.shujutang.highway.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nancr on 2015/8/3.
 */
public class RegularUtil {

    public static boolean match(String str, String regular){
        Pattern pattern = Pattern.compile(regular);
        Matcher matcher = pattern.matcher(str);
        boolean matches = matcher.matches();
        return matches;
    }
}
