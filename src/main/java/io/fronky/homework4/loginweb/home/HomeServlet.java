package io.fronky.homework4.loginweb.home;

import io.fronky.homework4.loginweb.Routable;
import io.fronky.homework4.loginweb.services.SecurityService;
import io.fronky.homework4.loginweb.users.User;
import io.fronky.homework4.loginweb.users.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class HomeServlet extends HttpServlet implements Routable {

    private static HomeServlet homeServlet = null;

    private SecurityService securityService;
    private UserService userService;

    private HomeServlet(){
        this.securityService = SecurityService.getInstance();
        this.userService = UserService.getInstance();
    }

    public static HomeServlet getInstance(){
        if (homeServlet == null)
        {
            //synchronized block to remove overhead
            synchronized (HomeServlet.class)
            {
                if(homeServlet==null)
                {
                    // if instance is null, initialize
                    homeServlet = new HomeServlet();
                }
            }
        }
        return homeServlet;
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            try{
                String username = (String) request.getSession().getAttribute("username");
                request.setAttribute("username", username);
                User user = userService.get(username);
                request.setAttribute("user", user);
                RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/home.jsp");
                rd.include(request, response);
            } catch(SQLException e){
                e.printStackTrace();
            }
        } else {
            response.sendRedirect("/login");
        }
    }

    @Override
    public String getMapping() {
        return "/index.jsp";
    }

}
