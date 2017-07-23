package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 *      注解注入值
 *
 * @author Liqn
 * @create 2017-07-16 15:57
 **/
public class IocHelper {

    static {
        Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();

        if (CollectionUtil.isNotEmpty(beanMap)){
            for (Map.Entry<Class<?>,Object> beanEntity : beanMap.entrySet()){
                Class<?> beanClass = beanEntity.getKey();
                Object  beanInstance = beanEntity.getValue();

                // 获取成员变量
                Field[] beanFields = beanClass.getDeclaredFields();

                if (ArrayUtil.isNotEmpty(beanFields)){
                    for (Field field : beanFields){
                        //判断当前Bean Field 是否带Inject注解
                        if (field.isAnnotationPresent(Inject.class)){
                            Class<?> beanFieldClass = field.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance!=null){
                                ReflectionUtil.setField(beanInstance,field,beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }


}
