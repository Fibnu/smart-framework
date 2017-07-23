package org.smart4j.framework.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.PropsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Liqn
 * @create 2017-07-13 22:51
 **/
public class DatabaseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    private static final BasicDataSource DATA_SOURCE;

    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;
    static {

        CONNECTION_HOLDER = new ThreadLocal<Connection>();


        //  自动生成引用  ctrl + alt + v
        Properties properties = PropsUtil.loadProps("smart.properties");
        DRIVER = properties.getProperty("smart.framework.jdbc.driver");
        URL = properties.getProperty("smart.framework.jdbc.url");
        USERNAME = properties.getProperty("smart.framework.jdbc.username");
        PASSWORD = properties.getProperty("smart.framework.jdbc.password");


        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);

        // try catch  快捷键  ctrl+alt+t
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can not load jdbc driver",e);
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection(){
        Connection conn=CONNECTION_HOLDER.get();
        if(conn == null){
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure",e);
                throw  new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }


    /**
     *
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     *      查询实体列表
     */
    public static  <T>List<T> queryEntityList(Class<T> entityClass, String sql, Object... params){
        List<T> entityList=null;
        try {
            entityList = QUERY_RUNNER.query(getConnection(),sql,new BeanListHandler<T>(entityClass),params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure",e);
            throw  new RuntimeException(e);
        }
        return entityList;
    }


    /**
     * @param sql
     * @param params
     * @return
     *  执行更新语句(包括 update,insert,delete)
     *
     *
     */
    public static int executeUpdate(String sql,Object... params){
        int row =0;
        Connection conn = getConnection();
        try {
            row =QUERY_RUNNER.update(conn,sql,params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure",e);
            throw new RuntimeException(e);
        }
        return row;
    }


    public static <T> boolean insertEntity(Class<T> entityClass, Map<String,Object> filedMap){
        if (CollectionUtil.isEmpty(filedMap)){
            LOGGER.error("can not insert entiry:fieldMap is empty");
            return false;
        }
        String sql = "INSERT INTO " + getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");

        for (String fieldName:filedMap.keySet()){
            columns.append(fieldName).append(",");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(","),columns.length(),")");
        values.replace(values.lastIndexOf(","),values.length(),")");

        sql += columns + "VALUES" + values;
        Object[] params = filedMap.values().toArray();

        return executeUpdate(sql,params) == 1;
    }


    public static <T> boolean updateEntity(Class<T> entityClass,long id ,Map<String ,Object> fieldMap){
        if (CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not update entity:fieldMap is empty");
            return false;
        }
        String sql = "UPDATE " +getTableName(entityClass)+" SET ";
        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet()){
            columns.append(fieldName).append("=?,");
        }
        sql +=columns.substring(0,columns.lastIndexOf(",")) + "WHERE ID = ?";

        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();
        LOGGER.info("update is over");
        return executeUpdate(sql,params) == 1;
    }



    public static String getTableName(Class entityClass){
        return entityClass.getSimpleName();
    }


    public static void executeFileSql(String filePath){
        InputStream in =  Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String sql="";
        try{
            while ((sql = reader.readLine())!=null){
                DatabaseHelper.executeUpdate(sql);
            }
        }catch (IOException e){
            LOGGER.error("execute file sql error",e);
        }

    }

    /**
     * 开启事务
     */
    public static void beginTransaction(){
        Connection conn = getConnection();
        if (conn!=null){
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                LOGGER.error("begin transaction failure",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
    }


    /**
     * 提交事务
     */
    public static void commitTransaction(){
        Connection conn = getConnection();
        if (conn != null){
            try {
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("commit transaction failure",e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }


    /**
     * 回滚事务
     */
    public static void rollbackTransaction(){
        Connection conn = getConnection();
        if (conn != null){
            try {
                conn.rollback();
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("rollback transaction failure",e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }



}
