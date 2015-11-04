<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>
    <title>Авторизация пользователя</title>
    <style type="text/css">
        @import url(<c:url value="/resources/style/tackboardLoginStyle.css"/>);
    </style>
</head>
<body background='<c:url value="/resources/images/Fence.jpg"/>'>
<div id="header">
    <h1>Авторизация пользователя</h1>
    <% try { %>
    <% if(session.getAttribute("errortype").equals("wrongpassword")) { %>
    <script type="text/javascript">alert('Такого пользователя не существует');</script>
    <% } else if (session.getAttribute("errortype").equals("invalidpassword")){ %>
    <script type="text/javascript"
            >alert('Соблюдайте формат ввода!\nЛогин и пароль должны начинаться с латинской буквы\nи состоять из латинских букв и цифр');</script>
    <% } %>
    <% } catch (Exception ex) {} %>
    <% session.setAttribute("errortype",null); %>
    <form action="/authenticate.html" method="post">
        <p>Ваш логин</p><input type="text" maxlength="30" name="log" pattern="^[a-zA-Z0-9]+$"
                               title="Только латинские буквы и цифры" required/><br>

        <p>Ваш пароль</p><input type="password" maxlength="30" name="pass" pattern="^[a-zA-Z0-9]+$"
                                title="Только латинские буквы и цифры" required/><br><br>
        <input type="submit" value="Авторизация" size="60"/>
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
