package io.fronky.homework4.loginweb.register;

import io.fronky.homework4.loginweb.Routable;
import io.fronky.homework4.loginweb.services.SecurityService;
import io.fronky.homework4.loginweb.users.UserService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet implements Routable {

    private static RegisterServlet registerServlet = null;

    private UserService userService;
    private SecurityService securityService;

    private RegisterServlet(){
        this.securityService = SecurityService.getInstance();
        this.userService = UserService.getInstance();
    }

    public static RegisterServlet getInstance(){
        if (registerServlet == null)
        {
            //synchronized block to remove overhead
            synchronized (RegisterServlet.class)
            {
                if(registerServlet==null)
                {
                    // if instance is null, initialize
                    registerServlet = new RegisterServlet();
                }
            }
        }
        return registerServlet;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if(authorized){
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/register.jsp");
            rd.include(request, response);
        }
        else{
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if(authorized){
            try{
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                if (!StringUtils.isBlank(username) && !StringUtils.isBlank(password) &&
                    !StringUtils.isBlank(firstName) && !StringUtils.isBlank(lastName) ) {
                    if (userService.containsUser(username)) {
                        String error = "Username " + username + " already exists";
                        request.setAttribute("error", error);
                        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/register.jsp");
                        rd.include(request, response);
                    } else {
                        userService.create(username, password, firstName, lastName);
                        response.sendRedirect("/users");
                    }
                } else {
                    String error = "Some fields are missing.";
                    request.setAttribute("error", error);
                    RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/register.jsp");
                    rd.include(request, response);
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        else{
            response.sendRedirect("/login");
        }

    }

    @Override
    public String getMapping() {
        return "/register";
    }
}
