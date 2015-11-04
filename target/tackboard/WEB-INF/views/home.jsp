<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>
    <title>Доска объявлений</title>
    <style type="text/css">
        @import url(<c:url value="/resources/style/tackboardLoginStyle.css"/>);
    </style>
    <script type="text/javascript" src="/resources/script/buttons.js"></script>
</head>
<body background='<c:url value="/resources/images/Fence.jpg"/>'>
<div id="header"><h1>ТЕСТОВОЕ ЗАДАНИЕ<br><br>ДОСКА ОБЪЯВЛЕНИЙ<br></h1>
    <table width="100%">
        <tr>
            <td width="33%" height="150px" valign="center" align="center">
                <a href="/login.html">
                    <button id="login" onmouseover='action1("login")' onmouseout='action2("login")'>ВХОД</button>
                </a>
            </td>
            <td width="33%" valign="center" align="center">
                <a href="/register.html">
                    <button id="reg" onmouseover='action1("reg")' onmouseout='action2("reg")'>РЕГИСТРАЦИЯ</button>
                </a>
            </td>
            <td valign="center" align="center">
                <a href="/ads.html">
                    <button id="stare" onmouseover='action1("stare")' onmouseout='action2("stare")'>Просмотр<br>объявлений
                    </button>
                </a>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
