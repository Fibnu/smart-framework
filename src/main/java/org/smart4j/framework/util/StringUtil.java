package org.smart4j.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Liqn
 * @create 2017-07-12 23:36
 **/
public class StringUtil {
    public static boolean isEmpty(String str){
        if(str != null){
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }


    public static String[] splitString(String source,String splitType){
        if (StringUtil.isEmpty(source)){
            return null;
        }
        if (StringUtil.isEmpty(splitType)){
            return new String[]{source};
        }
        return source.split(splitType);
    }

}
