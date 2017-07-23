package org.smart4j.framework.proxy;

/**
 * Created by Administrator on 2017/7/17.
 */
public interface Proxy {

    /**
     * 执行链式代理
     * @return
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;

}
