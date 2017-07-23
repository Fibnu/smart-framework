package org.smart4j.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.proxy.AspectProxy;
import org.smart4j.framework.proxy.Proxy;
import org.smart4j.framework.proxy.ProxyManager;
import org.smart4j.framework.proxy.TransactionProxy;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author Liqn
 * @create 2017-07-17 22:32
 **/
public class AopHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    static {
        try{
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()){
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                BeanHelper.setBean(targetClass,proxy);
            }
        }catch (Exception e){
            LOGGER.error("aop error",e);
        }
    }


    // 使用该Aspect注解的所有目标类
    private static Set<Class<?>> createTargetClassSet(Aspect aspect){
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();

        Class<? extends Annotation> annotation = aspect.value();

        if (annotation != null && !annotation.equals(Aspect.class)){
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }

        return targetClassSet;
    }

    /**
     * 获取代理类及其目标类集合之间的映射关系
     *
     *      一个代理类 对应  多个目标类
     */
    private static Map<Class<?>,Set<Class<?>>> createProxyMap()throws Exception{
        Map<Class<?>,Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();

        addAsepectProxy(proxyMap);
        addTransactionProxy(proxyMap);


        return proxyMap;
    }

    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
        // 获取代理类
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetByAnnotation(Service.class);
        doAddProxy(proxyClassSet,proxyMap);
    }

    private static void addAsepectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {

        // 获取代理类
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        doAddProxy(proxyClassSet,proxyMap);

    }

    private static void doAddProxy(Set<Class<?>> proxyClassSet, Map<Class<?>, Set<Class<?>>> proxyMap) {
        for (Class<?> proxyClass : proxyClassSet){
            if (proxyClass.isAnnotationPresent(Aspect.class)){
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass,targetClassSet);
            }
        }
    }


    /**
     *  目标类与代理对象之间的映射关系
     */
    private static Map<Class<?>,List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap)throws Exception{
        Map<Class<?>,List<Proxy>> map = new HashMap<Class<?>, List<Proxy>>();

        for (Map.Entry<Class<?>,Set<Class<?>>> entry : proxyMap.entrySet()){
            Class<?> proxyClass = entry.getKey();
            Set<Class<?>> targetClassSet = entry.getValue();

            for (Class<?> targetClass : targetClassSet){
                Proxy proxy = (Proxy)proxyClass.newInstance();
                if (map.containsKey(targetClass)){
                    map.get(targetClass).add(proxy);
                }else {
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    map.put(targetClass,proxyList);
                }
            }
        }

        return map;

    }




}
