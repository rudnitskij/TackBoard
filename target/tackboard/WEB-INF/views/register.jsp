<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>
    <title>Регистрация</title>
    <style type="text/css">
        @import url(<c:url value="/resources/style/tackboardLoginStyle.css"/>);
    </style>
</head>
<body background='<c:url value="/resources/images/Fence.jpg"/>'>
<% try { %>
<% if(session.getAttribute("errortype").equals("invalidpassword")){ %>
<h1>Соблюдайте формат ввода!<br>Логин и пароль должны начинаться с латинской буквы<br>и состоять из латинских букв и цифр</h1>
<% } %>
<% } catch (Exception ex) {} %>
<% session.setAttribute("errortype",null); %>
<p id="style2">
    Введите свой логин и пароль для регистрации<br>нового пользователя в базе данных
</p>
<div id="header">
    <form action="/addUser.html" method="post">
        <p>Ваш логин</p><input type="text" maxlength="30" name="log" pattern="^[a-zA-Z][a-zA-Z0-9]+$"
                               title="Только латинские буквы и цифры" required/><br>

        <p>Ваш пароль</p><input type="password" maxlength="30" name="pass" pattern="^[a-zA-Z][a-zA-Z0-9]+$"
                                title="Только латинские буквы и цифры" required/><br><br>
        <input type="submit" value="Регистрация" size="60"/>
    </form>
    <br><br><br><br><br><br>
    <table width="100%">
        <tr>
            <td width="33%" height="120px"></td>
            <td>
                <a href="/ads">
                    <button>К объявлениям<br>без авторизации</button>
                </a>
            </td>
            <td width="30%"></td>

        </tr>
    </table>
</div>
</body>
</html>
