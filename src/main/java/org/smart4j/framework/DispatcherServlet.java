package org.smart4j.framework;

import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Liqn
 * @create 2017-07-16 20:14
 *
 *     转发器
 **/
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet{

    @Override
    public void init(ServletConfig config) throws ServletException {
        HelperLoader.init();

        ServletContext servletContext = config.getServletContext();

        // 注册处理jsp的servler
        ServletRegistration jsp = servletContext.getServletRegistration("jsp");

        jsp.addMapping(ConfigHelper.getAppJspPath()+"*");

        // 注册处理静态资源的默认servlet
        ServletRegistration aDefault = servletContext.getServletRegistration("default");

        aDefault.addMapping(ConfigHelper.getAppAssetPath());

    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求方法和路径
        String reqMethod = req.getMethod().toLowerCase();
        String reqPath = req.getPathInfo();

        // 获取action处理器
        Handler handler = ControllerHelper.getHandler(reqMethod, reqPath);

        if (handler != null){
            //获取controller 控制类
            Class<?> controllerClass = handler.getControllerClass();

            // 得到 框架管理的Bean实例
            Object controllerBean = BeanHelper.getBean(controllerClass);

            // 创建请求参数对象
            Map<String,Object> paramMap = new HashMap<String, Object>();
            Enumeration<String> parameterNames = req.getParameterNames();

            while (parameterNames.hasMoreElements()){
                String paramName = parameterNames.nextElement();
                String paramValue = req.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }


            String body = CodeUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if (StringUtil.isNotEmpty(body)){
                String[] params = StringUtil.splitString(body,"&");
                if (ArrayUtil.isNotEmpty(params)){
                    for (String param:params){
                        String[] array = StringUtil.splitString(param,"=");
                        paramMap.put(array[0],array[1]);
                    }
                }
            }

            Param param =  new Param(paramMap);

            // 调用action方法
            Method method = handler.getActionMethod();

            Object result;
            if (param.isEmpty()){
                result = ReflectionUtil.invokeMethod(controllerBean, method);
            }else{
                result = ReflectionUtil.invokeMethod(controllerBean, method, param);
            }


            if (result instanceof View){
                View view = (View)result;
                String path = view.getPath();

                if (StringUtil.isNotEmpty(path)){
                    if (path.startsWith("/")){
                        resp.sendRedirect(req.getContextPath()+path);
                    }else {
                        Map<String, Object> model = view.getModel();
                        for (Map.Entry<String,Object> entry:model.entrySet()){
                            req.setAttribute(entry.getKey(),entry.getValue());
                        }
                        String s = ConfigHelper.getAppJspPath()+path;
                        req.getRequestDispatcher(s).forward(req,resp);
                    }
                }

            }else if (result instanceof Data){
                Data data = (Data)result;
                Object model = data.getModel();
                if (model != null){
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("utf-8");
                    PrintWriter writer = resp.getWriter();

                    String json = JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }


            }

        }


    }
}
