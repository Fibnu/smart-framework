package org.smart4j.framework.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Liqn
 * @create 2017-07-16 20:03
 *
 * 返回视图对象
 **/
public class View {

    private String path;
    private Map<String,Object> model;

    public View(String path) {
        this.path = path;
        model = new HashMap<String, Object>();
    }

    public View addModel(String key,Object obj){
        model.put(key,obj);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
