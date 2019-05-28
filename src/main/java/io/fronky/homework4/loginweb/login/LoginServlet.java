package io.fronky.homework4.loginweb.login;

import io.fronky.homework4.loginweb.Routable;
import io.fronky.homework4.loginweb.services.SecurityService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet implements Routable {

    private static LoginServlet loginServlet = null;

    private SecurityService securityService;

    private LoginServlet(){
        this.securityService = SecurityService.getInstance();
    }

    public static LoginServlet getInstance(){
        if (loginServlet == null)
        {
            //synchronized block to remove overhead
            synchronized (LoginServlet.class)
            {
                if(loginServlet==null)
                {
                    // if instance is null, initialize
                    loginServlet = new LoginServlet();
                }
            }
        }
        return loginServlet;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if(!authorized){
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/login.jsp");
            rd.include(request, response);
        }
        else{
            response.sendRedirect("/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // do login post logic
        // extract username and password from request
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (!StringUtils.isBlank(username) && !StringUtils.isBlank(password)) {
            if (securityService.authenticate(username, password, request)) {
                response.sendRedirect("/");
            } else {
                String error = "Wrong username or password.";
                request.setAttribute("error", error);
                RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/login.jsp");
                rd.include(request, response);
            }
        } else {
            String error = "Username or password is missing.";
            request.setAttribute("error", error);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/login.jsp");
            rd.include(request, response);
        }

        // check username and password against database
        // if valid then set username attribute to session via securityService
        // else put error message to render error on the login form

    }

    @Override
    public String getMapping() {
        return "/login";
    }

}
