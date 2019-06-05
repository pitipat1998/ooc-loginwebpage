package io.fronky.homework4.loginweb.home;

import com.google.gson.Gson;
import io.fronky.homework4.loginweb.Routable;
import io.fronky.homework4.loginweb.services.SecurityService;
import io.fronky.homework4.loginweb.users.User;
import io.fronky.homework4.loginweb.users.UserService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

public class EditUserServlet extends HttpServlet implements Routable {

    private static EditUserServlet editUserServlet = null;

    private SecurityService securityService;
    private UserService userService;

    private EditUserServlet(){
        this.securityService = SecurityService.getInstance();
        this.userService = UserService.getInstance();
    }

    public static EditUserServlet getInstance(){
        if (editUserServlet == null)
        {
            //synchronized block to remove overhead
            synchronized (EditUserServlet.class)
            {
                if(editUserServlet==null)
                {
                    // if instance is null, initialize
                    editUserServlet = new EditUserServlet();
                }
            }
        }
        return editUserServlet;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            try{
                String username = (String) request.getSession().getAttribute("username");
                User user = userService.get(username);
                request.setAttribute("user", user);
                RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/edituser.jsp");
                rd.include(request, response);
            } catch(SQLException e){
                e.printStackTrace();
            }
        } else {
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            try{
                String username = (String) request.getSession().getAttribute("username");
                String password = request.getParameter("password");
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                if (!StringUtils.isBlank(password) && !StringUtils.isBlank(firstName) && !StringUtils.isBlank(lastName) ) {
                    userService.update(username, password, firstName, lastName);
                    response.sendRedirect("/user");
                } else {
                    String error = "Some fields are missing.";
                    request.setAttribute("error", error);
                    RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/register.jsp");
                    rd.include(request, response);
                }
                } catch(SQLException e){
                    e.printStackTrace();
                }

        } else {
            response.sendRedirect("/login");
        }
    }

    @Override
    public String getMapping() {
        return "/edit";
    }
}
