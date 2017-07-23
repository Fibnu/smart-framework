package org.smart4j.framework.helper;

import org.smart4j.framework.ConfigConstant;
import org.smart4j.framework.util.PropsUtil;

import java.util.Properties;

/**
 * @author Liqn
 * @create 2017-07-15 23:09
 *
 *      属性文件 助手类
 **/
public class ConfigHelper {
    private static final Properties CONF_PROPS  = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);

    public static String getJdbcDriver(){
        return PropsUtil.getString(CONF_PROPS,ConfigConstant.JDBC_DRIVER);
    }

    public static String getJdbcUrl(){
        return PropsUtil.getString(CONF_PROPS,ConfigConstant.JDBC_URL);
    }

    public static String getJdbcUserName(){
        return PropsUtil.getString(CONF_PROPS,ConfigConstant.JDBC_USERNAME);
    }

    public static String getJdbcPassword(){
        return PropsUtil.getString(CONF_PROPS,ConfigConstant.JDBC_PASSWORD);
    }

    public static String getAppBasePackage(){
        return PropsUtil.getString(CONF_PROPS,ConfigConstant.APP_BASE_PACKAGE);
    }

    public static String getAppJspPath(){
        return PropsUtil.getString(CONF_PROPS,ConfigConstant.APP_JSP_PATH,"/WEB-INF/view/");
    }

    public static String getAppAssetPath(){
        return PropsUtil.getString(CONF_PROPS,ConfigConstant.APP_ASSET_PATH,"/asset/");
    }


}
