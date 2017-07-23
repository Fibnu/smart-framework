package org.smart4j.framework.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Liqn
 * @create 2017-07-16 16:19
 *
 *  数组工具类
 **/
public class ArrayUtil {

    public static boolean isEmpty(Object[] objs){
        return ArrayUtils.isEmpty(objs);
    }

    public static boolean isNotEmpty(Object[] objs){
        return !isEmpty(objs);
    }



}
