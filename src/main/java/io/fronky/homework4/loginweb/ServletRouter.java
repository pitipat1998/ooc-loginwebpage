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
import io.fronky.homework4.loginweb.users.UserServlet;
import io.fronky.homework4.loginweb.users.UsersServlet;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author gigadot
 */
public class ServletRouter implements Filter {

    private static final List<Class<? extends Routable>> routables = new ArrayList<>();
    private static Map<String, HttpServlet> urls = new ConcurrentHashMap<>();

    static {
        routables.add(HomeServlet.class);
        routables.add(LoginServlet.class);
        routables.add(UserServlet.class);
        routables.add(UsersServlet.class);
        routables.add(LogoutServlet.class);
        routables.add(RegisterServlet.class);
        routables.add(EditUserServlet.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url = request.getRequestURI();
        if(!urls.containsKey(url)){
            response.sendRedirect("/users");
        }
        else{
            urls.get(url).service(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {

    }

    public void init(Context ctx) {
        for (Class<? extends Routable> routableClass : routables) {
            try {
                Method method = routableClass.getMethod("getInstance", null);
                Routable routable = (Routable) method.invoke(null, null);
                String name = routable.getClass().getSimpleName();
                Tomcat.addServlet(ctx, name, (HttpServlet) routable);
                ctx.addServletMappingDecoded(routable.getMapping(), name);
                urls.put(routable.getMapping(), (HttpServlet) routable);
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
