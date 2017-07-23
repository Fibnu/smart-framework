package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author Liqn
 * @create 2017-07-16 20:52
 **/
public class CodeUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CodeUtil.class);

    /**
     * 将url编码
     */
    public static String encodeURL(String source){
        String target;
        try{
           target = URLEncoder.encode(source, "utf-8");
        }catch (Exception e){
            LOGGER.error("encode url failure",e);
            throw new RuntimeException(e);
        }

        return target;
    }
    /**
     * url 解码
     */
    public static String decodeURL(String source){
        String target;
        try{
            target = URLDecoder.decode(source, "utf-8");
        }catch (Exception e){
            LOGGER.error("decoder url failure",e);
            throw new RuntimeException(e);
        }

        return target;
    }




}
