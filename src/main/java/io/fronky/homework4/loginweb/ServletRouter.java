/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.fronky.homework4.loginweb;

import io.fronky.homework4.loginweb.home.EditUserServlet;
import io.fronky.homework4.loginweb.home.HomeServlet;
import io.fronky.homework4.loginweb.login.LoginServlet;
import io.fronky.homework4.loginweb.logout.LogoutServlet;
import io.fronky.homework4.loginweb.register.RegisterServlet;
import io.fronky.homework4.loginweb.services.SecurityService;
import io.fronky.homework4.loginweb.users.UsersServlet;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.http.HttpServlet;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gigadot
 */
public class ServletRouter {

    private static final List<Class<? extends Routable>> routables = new ArrayList<>();

    static {
        routables.add(HomeServlet.class);
        routables.add(LoginServlet.class);
        routables.add(UsersServlet.class);
        routables.add(LogoutServlet.class);
        routables.add(RegisterServlet.class);
        routables.add(EditUserServlet.class);
    }

    public void init(Context ctx) {
        for (Class<? extends Routable> routableClass : routables) {
            try {
                Method method = routableClass.getMethod("getInstance", null);
                Routable routable = (Routable) method.invoke(null, null);
                String name = routable.getClass().getSimpleName();
                Tomcat.addServlet(ctx, name, (HttpServlet) routable);
                ctx.addServletMapping(routable.getMapping(), name);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e){
                e.printStackTrace();
            } catch (InvocationTargetException e){
                e.printStackTrace();
            }
        }
    }

}
