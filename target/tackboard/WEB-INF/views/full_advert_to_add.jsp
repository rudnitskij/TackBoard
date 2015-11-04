<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="morgun.dev.tackboard.Advert" %>
<%@ page import="static morgun.dev.tackboard.DBaseHandling.getLastAddedAdvert" %>
<%@ page import="static morgun.dev.tackboard.Advert.getTopicRus" %>
<%@ page import="static morgun.dev.tackboard.BoardController.userMap" %>
<%@ page import="java.util.Date" %>
<%  Advert advert = getLastAddedAdvert();
    Date date;
    String dateString;
    boolean anon = userMap.get(session.getId()).equals("anonymous"); %>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>
    <title>Добавление нового объявления</title>
    <style type="text/css">
        @import url(<c:url value="/resources/style/tackboardLoginStyle.css"/>);
    </style>
    <script type="text/javascript" src="/resources/script/buttons.js"></script>
</head>
<body background='<c:url value="/resources/images/Fence.jpg"/>'>
<div id="loginPanel">
    <%=userMap.get(session.getId())%>
</div>
<% if(!anon){ %>
<div id="logoutButton"><a href="/logout.html">Выйти</a></div>
<% } %>
<% if (session.getAttribute("added").equals("false") || (advert.getAuthor().equals("not found"))) {
%> <p id="header">Ошибка, добавить объявление не удалось</p> <%
    session.setAttribute("added","true");
} else {
%>
<table id="header" width="100%">
    <tr id="tdCenter">
        <td width="25%" height="">Меню<br></td>
        <td valign="top">Объявления</td>
    </tr>
    <tr>
        <td valign="top">
            <div id="navcontainer" align="left">
                <ul>
                    <li><a>Добавить объявление</a></li>
                    <li><a <% if(anon) { %> href="/login.html" <% } %> >Авторизация</a></li>
                    <li><a <% if (anon) { %> href="/register" <% } %> >Регистрация</a></li>
                </ul>
            </div>
        </td>
        <td align="center" valign="top">
            <table id="header" width="100%">
                <tr>
                    <td width="33%" align="left" height="40px">
                        <%=advert.getAuthor() %> <%--автор--%>
                    </td>
                    <td align="center">
                        <b><%=getTopicRus(advert.getTopic())%>
                        </b><%--рубрика--%>
                    </td>
                </tr>
                <tr>
                    <td width="33%" align="left" height="40px">
                        <% date = advert.getIssueDate().getTime();%>
                        <% dateString = String.format("%td-%tB-%tY<br>%tR", date, date, date, date);%>
                        <%=dateString %> <%--дата и время--%>
                    </td>
                    <td align="center">
                        <b><%=advert.getHeader()%>
                        </b> <%--заголовок--%>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <% session.setAttribute("pictureName",advert.getPictureLink()); %>
                        <img src="/picture" height="400px"> <%--картинка--%>
                    </td>
                </tr>
                <tr>
                    <td align="left" colspan="2">
                        <br><br>
                        <%=advert.getText()%> <%--текст объявления--%>
                        <br><br><br>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<% session.setAttribute("advert",advert); %>
<table width="100%" id="style2">
    <tr>
        <td width="33%" valign="center" height="80px">
            <a href="/edit.html">
                <button id="login" onmouseover='action1("login")' onmouseout='action2("login")'>Редактировать</button>
            </a>
        </td>
        <td width="33%" valign="center">
            <a href="">
                <button id="reg" onmouseover='action1("reg")' onmouseout='action2("reg")'>Удалить</button>
            </a>
        </td>
        <td valign="center"><a href="/ads.html">
            <button id="stare" onmouseover='action1("stare")' onmouseout='action2("stare")'>К объявлениям
            </button>
        </a>
        </td>
    </tr>
</table>
<% } %>
</body>
</html>
