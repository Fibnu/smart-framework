package org.smart4j.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author Liqn
 * @create 2017-07-17 20:48
 **/
public abstract class AspectProxy implements Proxy{

    private static final Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);

    public Object doProxy(ProxyChain proxyChain) throws Throwable{
        Object result=null;
        Object[] methodParams = proxyChain.getMethodParams();
        Class<?> cls = proxyChain.getTargetClass();
        Method targetMethod = proxyChain.getTargetMethod();

        begin();

        try{
            if (intercept(cls,targetMethod,methodParams)){
                before(cls,targetMethod,methodParams);
                result = proxyChain.doProxyChain();
                after(cls,targetMethod,methodParams);
            } else {
                result =  proxyChain.doProxyChain();
            }

        }catch (Exception e){
            LOGGER.error("proxy failure",e);
            error(cls,targetMethod,methodParams);
            throw  e;
        }finally {
            end();
        }
        
        return result;



    }

    public void end() {
    }

    public void error(Class<?> cls, Method targetMethod, Object[] methodParams) {
    }

    public void after(Class<?> cls, Method targetMethod, Object[] methodParams) {
    }

    public void before(Class<?> cls, Method targetMethod, Object[] methodParams) {
    }

    public boolean intercept(Class<?> cls, Method targetMethod, Object[] methodParams) {
        return  true;
    }

    public void begin() {

    }
}
