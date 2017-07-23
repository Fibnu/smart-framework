package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Request;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Liqn
 * @create 2017-07-16 16:33
 **/
public class ControllerHelper {

    /**
     * 用于存放请求与处理器的映射关系
     */
    private static  final Map<Request,Handler> ACTION_MAP = new HashMap<Request,Handler>();


    static {
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();

        if (CollectionUtil.isNotEmpty(controllerClassSet)){
            for (Class<?> cls : controllerClassSet){
                Method[] methods = cls.getDeclaredMethods();

                if (ArrayUtil.isNotEmpty(methods)){
                    for (Method method : methods){
                        if (method.isAnnotationPresent(Action.class)){
                            Action action  = method.getAnnotation(Action.class);
                            String mapping = action.value();

                            // 验证url规则
                            if (mapping.matches("\\w+:/\\w*")){
                                String[] array = mapping.split(":");

                                if (ArrayUtil.isNotEmpty(array) && array.length==2){
                                    String requestMethod = array[0];
                                    String requestPath = array[1];

                                    Request request = new Request(requestMethod,requestPath);

                                    Handler handler = new Handler(cls,method);


                                    ACTION_MAP.put(request,handler);

                                }

                            }

                        }
                    }
                }

            }
        }


    }


    public static Handler getHandler(String requestMethod,String requestPath){
        Request request = new Request(requestMethod,requestPath);
        return ACTION_MAP.get(request);

    }




}
