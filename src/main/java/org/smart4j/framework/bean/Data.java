package org.smart4j.framework.bean;

/**
 * @author Liqn
 * @create 2017-07-16 20:08
 *
 *  返回数据对象
 *
 **/
public class Data {

    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
