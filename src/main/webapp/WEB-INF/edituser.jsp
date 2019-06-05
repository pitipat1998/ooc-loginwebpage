<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <ul>
        <li><a href="/user">User</a></li>
        <li><a href="/users">Users</a></li>
    </ul>
    <body>
        <h1>Edit</h1>
        <p>${error}</p>
        <h2>Username:</h2>
        <h3>${user.username}</h3>
        <form action="/edit" method="post">
            <h2>Password</h2>
            <input type="password" name="password" id="userPassword"/>
            <br/>
            <h2>First Name</h2>
            <input type="text" name="firstName" id="firstName" value="${user.firstName}"/>
            <br/>
            <h2>Last Name</h2>
            <input type="text" name="lastName" id="lastName" value="${user.lastName}"/>
            <br><br>
            <input type="submit" value="Submit">
        </form>
    </body>
</html>