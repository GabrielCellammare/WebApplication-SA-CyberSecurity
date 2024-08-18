<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Login Web Application</title>
        <style>
         	<%@include file="/WEB-INF/css/styleLogin.css"%>
         </style>
    </head>
    <body>
        <div class="container">
            <h1>Login Web Application</h1>
            <form action="LoginServlet" method="post">
                <div class="field">
                    <label for="email">Email</label>
                    <input type="email" name="email" id="email" required>
                </div>
                <div class="field">
                    <label for="password">Password</label>
                    <input type="password" name="password" id="password" required>
                </div>
                <div class="field">
                    <input type="checkbox" name="remember" id="remember">
                    <label for="remember">Ricordami</label>
                </div>
                <input type="submit" value="Accedi">
                <a href="userNotLoggedIndex.jsp" class="btn">Indietro</a>
            </form>
        </div>
    </body>
</html>
