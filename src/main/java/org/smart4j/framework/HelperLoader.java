package org.smart4j.framework;

import org.smart4j.framework.helper.*;
import org.smart4j.framework.util.ClassUtil;

/**
 * @author Liqn
 * @create 2017-07-16 17:17
 *
 *  AOP 在IOC 之前， AOP取代目标对象后，才能IOC赋值
 **/
public class HelperLoader {

    public static void init(){
        Class<?>[] classList = {ClassHelper.class,
                                BeanHelper.class,
                                AopHelper.class,
                                IocHelper.class,
                                ControllerHelper.class};
        for (Class cls : classList){
            ClassUtil.loadClass(cls.getName(),true);
        }
    }

}
