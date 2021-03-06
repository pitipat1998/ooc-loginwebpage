package io.fronky.homework4.loginweb;

import io.fronky.homework4.loginweb.services.DataService;
import io.fronky.homework4.loginweb.services.SecurityService;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.ServletException;
import java.io.File;
import java.sql.SQLException;
import java.util.Properties;

public class Application {
    public static void main(String[] args) {
        File docBase = new File("src/main/webapp/");
        docBase.mkdirs();
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8082);

        String domain = "db";
        String dbName = "webapp";
        int port = 3306;
        String dbDriver = "com.mysql.jdbc.Driver";
        String connString = "jdbc:mysql://"+domain+":"+port+"/";
        Properties connProps = new Properties();
        connProps.setProperty("user", "root");
        connProps.setProperty("password", "12345");

        DataService dataService;
        try{
            dataService = new DataService(connString, dbName, dbDriver, connProps);
            dataService.init();
        }catch (Exception e){
            e.printStackTrace();
        }

        ServletRouter servletRouter = new ServletRouter();
        Context ctx;

        try {
            ctx = tomcat.addWebapp("", docBase.getAbsolutePath());
            servletRouter.init(ctx);

            Class filterClass = ServletRouter.class;
            FilterDef myFilterDef = new FilterDef();
            myFilterDef.setFilterClass(filterClass.getName());
            myFilterDef.setFilterName(filterClass.getSimpleName());
            ctx.addFilterDef(myFilterDef);

            FilterMap myFilterMap = new FilterMap();
            myFilterMap.setFilterName(filterClass.getSimpleName());
            myFilterMap.addURLPattern("/*");
            ctx.addFilterMap(myFilterMap);

            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException ex) {
            ex.printStackTrace();
        }
    }
}
