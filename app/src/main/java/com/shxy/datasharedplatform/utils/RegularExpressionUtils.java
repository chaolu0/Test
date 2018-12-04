package com.shxy.datasharedplatform.utils;

import java.util.regex.Pattern;

/**
 * Created by shxy on 2018/12/4.
 */

public class RegularExpressionUtils {

    public static boolean matchEmail(String src){
       return Pattern.matches(".+@.+\\..+",src);
    }
}
