package org.smart4j.framework;

/**
 * @author Liqn
 * @create 2017-07-15 22:53
 *
 *      相关配置项常量
 *
 *   常量可以用接口封装
 **/
public interface ConfigConstant {


    //  配置文件
    String CONFIG_FILE = "smart.properties";

    // 数据库驱动
    String JDBC_DRIVER="smart.framework.jdbc.driver";
    // 数据库URL
    String JDBC_URL="smart.framework.jdbc.url";

    // 数据库 用户名
    String JDBC_USERNAME="smart.framework.jdbc.username";
    // 数据库 密码
    String JDBC_PASSWORD="smart.framework.jdbc.password";

    // 项目基础包名
    String APP_BASE_PACKAGE="smart.framework.app.base_package";
    // JSP路径
    String APP_JSP_PATH="smart.framework.app.jsp_path";
    // 静态资源文件的基础路径  比如js  css 图片
    String APP_ASSET_PATH="smart.framework.app.asset_path";



}
