package org.smart4j.framework.util;

/**
 * @author Liqn
 * @create 2017-07-12 23:19
 **/
public class CastUtil {

    public  static String caseString(Object obj){
        return CastUtil.caseString(obj,"");
    }

    public  static String caseString(Object obj,String defaultValue){
        return obj != null ?String.valueOf(obj):defaultValue;
    }

    public static double castDouble(Object obj){
        return CastUtil.castDouble(obj,0);
    }

    public static double castDouble(Object obj,double defaultValue){
        double doubleValue = defaultValue;
        if(obj != null){
            String strValue = CastUtil.caseString(obj);
            if (StringUtil.isNotEmpty(strValue)){
                try {
                    doubleValue = Double.parseDouble(strValue);
                } catch (NumberFormatException e) {
                    doubleValue = defaultValue;
                }
            }
        }
        return doubleValue;
    }

    public static long castLong(Object obj){
        return CastUtil.castLong(obj,0);
    }

    public static long castLong(Object obj,long defaultValue){
        long langValue = defaultValue;
        if(obj != null){
            String strValue = CastUtil.caseString(obj);
            if (StringUtil.isNotEmpty(strValue)){
                try {
                    langValue = Long.parseLong(strValue);
                } catch (NumberFormatException e) {
                    langValue = defaultValue;
                }
            }
        }
        return langValue;
    }

    public static int castInt(Object obj){
        return CastUtil.castInt(obj,0);
    }

    public static int castInt(Object obj,int defaultValue){
        int intValue = defaultValue;
        if(obj != null){
            String strValue = CastUtil.caseString(obj);
            if (StringUtil.isNotEmpty(strValue)){
                try {
                    intValue = Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }

    public static boolean castBoolean(Object obj){
        return CastUtil.castBoolean(obj,false);
    }

    public static boolean castBoolean(Object obj,boolean defaultValue){
        boolean booleanValue = defaultValue;
        if(obj != null){
            booleanValue = Boolean.parseBoolean(caseString(obj));
        }
        return booleanValue;
    }

}
