package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 *
 * @author Liqn
 * @create 2017-07-16 14:44
 **/
public class ReflectionUtil {

    private static  final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 创建对象
     * @param cls
     * @return
     */

    public static Object newInstance(Class<?> cls){
        Object instance;
        try {
            instance = cls.newInstance();
        } catch (Exception e) {
            LOGGER.error("new instance failure",e);
            throw new RuntimeException(e);
        }
        return  instance;
    }

    /**
     * 执行方法
     */
    public static Object invokeMethod(Object obj, Method method,Object... args){
        LOGGER.error("methodName:"+method.getName()+"====================");
        Object result;
        try {
            method.setAccessible(true);
            result = method.invoke(obj,args);
        } catch (Exception e) {
            LOGGER.error("method invoke error",e);
            throw new RuntimeException(e);
        }
        return result;
    }


    /**
     * 设置成员变量的值
     */
    public static void setField(Object obj, Field field,Object value){

        try {
            field.setAccessible(true);
            field.set(obj,value);
        } catch (IllegalAccessException e) {
            LOGGER.error("field set value is error",e);
            throw  new RuntimeException(e);
        }

    }





}
