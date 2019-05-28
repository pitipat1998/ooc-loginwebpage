<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <ul>
        <li><a href="/login">Login</a></li>
    </ul>
    <body>
        <h1>Register</h1>
        <p>${error}</p>
        <form action="/register" method="post">
        Username:<br/>
        <input type="text" name="username"/>
        <br/>
        Password:<br/>
        <input type="password" name="password"/>
        <br/>
        First Name:<br/>
        <input type="text" name="firstName"/>
        <br/>
        Last Name:<br/>
        <input type="text" name="lastName"/>
        <br><br>
        <input type="submit" value="Submit">
        </form>
    </body>
</html>