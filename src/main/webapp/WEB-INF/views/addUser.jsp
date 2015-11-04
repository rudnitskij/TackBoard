<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="static morgun.dev.tackboard.BoardController.userMap" %>
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
<div id="style2">
    <% if (session.getAttribute("registered") == "true") {%>
    <p>Поздравляем, вы зарегистрированы как новый пользователь <%=session.getAttribute("NewUser")%>
    </p>
    <a id="header" href="/ads.html">
        <button>К объявлениям</button>
    </a>
    <%userMap.put(session.getId(), (String) session.getAttribute("NewUser"));%>
    <%} else {%>
    <p>Ошибка регистрации</p>
    <a id="header" href="/index">
        <button>На стартовую страницу</button>
    </a>
    <%}%>
</div>
</body>
</html>
