<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <body>
        <ul>
            <li><a href="/users">Users</a> </li>
            <li><a href="/logout">Logout</a></li>
        </ul>
        <h1>Home</h1>
        <h2>Username:</h2>
        <h3>${user.username}</h3>
        <h2>First Name:</h2>
        <h3>${user.firstName}</h3>
        <h2>Last Name:</h2>
        <h3>${user.lastName}</h3>
        <a href="/edit">Edit</a>
    </body>
</html>