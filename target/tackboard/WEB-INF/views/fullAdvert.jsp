<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="static morgun.dev.tackboard.DBaseHandling.getAdvertById" %>
<%@ page import="java.util.Date" %>
<%@ page import="static morgun.dev.tackboard.Advert.getTopicRus" %>
<%@ page import="static morgun.dev.tackboard.BoardController.userMap" %>
<%@ page import="morgun.dev.tackboard.Advert" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% Date date; %>
<% String dateString; %>
<% Advert advert;%>
<% boolean logged; %>
<% boolean not_found; %>
<% boolean anon = userMap.get(session.getId()).equals("anonymous"); %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>
    <title>Полный вид</title>
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
<% advert = getAdvertById((Integer) session.getAttribute("advertID"));
    not_found = advert.getAuthor().equals("not found");
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
                    <li><a <% if(!anon) { %>  href="/new_advert.html" <% } %> >Добавить объявление</a></li>
                    <li><a <% if(anon) { %> href="/login.html" <% } %> >Авторизация</a></li>
                    <li><a <% if (anon) { %> href="/register" <% } %> >Регистрация</a></li>
                </ul>
            </div>
        </td>
        <td align="center" valign="top">
            <table id="header" width="100%">
                <tr>
                    <td width="33%" align="left" height="40px">
                        <%=(not_found) ? "Нет объявления" : advert.getAuthor() %> <%--автор--%>
                        <% logged = advert.getAuthor().equals(userMap.get(session.getId())); %>
                    </td>
                    <td align="center">
                        <% if(!not_found){ %>
                        <b><%=getTopicRus(advert.getTopic())%>
                        </b><%--рубрика--%>
                        <% } %>
                    </td>
                </tr>
                <tr>
                    <td width="33%" align="left" height="40px">
                        <% if(!not_found){ %>
                        <% date = advert.getIssueDate().getTime();%>
                        <% dateString = String.format("%td-%tB-%tY<br>%tR", date, date, date, date);%>
                        <%=dateString %> <%--дата и время--%>
                        <% } %>
                    </td>
                    <td align="center">
                        <% if(!not_found){ %>
                        <b><%=advert.getHeader()%>
                        </b> <%--заголовок--%>
                        <% } %>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <% if(!not_found){ %>
                        <% session.setAttribute("pictureName", advert.getPictureLink()); %>
                        <% } else {session.setAttribute("pictureName", "ads_not_found.jpg");} %>
                        <img src="/picture" height="400px"> <%--картинка--%>
                    </td>
                </tr>
                <tr>
                    <td align="left" colspan="2">
                        <% if(!not_found){ %>
                        <br><br>
                        <%=advert.getText()%> <%--текст объявления--%>
                        <br><br><br>
                        <% } %>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<% if(!not_found){ %>
<% session.setAttribute("advert",advert); %>
<% } %>
<table width="100%" id="style2">
    <tr>
        <td width="33%" valign="center" height="80px">
            <% if(!not_found){ %>
            <a <% if (logged) { %>href="/edit.html"<% } %>  >
                <button id="login" onmouseover='action1("login")' onmouseout='action2("login")' <% if(!logged) {%>
                        onclick="prohibited()"
                        <% } %> >Редактировать</button>
            </a>
            <% } %>
        </td>
        <td width="33%" valign="center">
            <% if(!not_found){ %>
            <a <% if (logged) { %>href="/remove.html"<% } %>  >
                <button id="reg" onmouseover='action1("reg")' onmouseout='action2("reg")' <% if(!logged) {%>
                        onclick="prohibited()"
                        <% } else { %> onclick="return confirm('Вы действительно хотите\nудалить объявление?')" <%  }%> >Удалить</button>
            </a>
            <% } %>
        </td>
        <td valign="center"><a href="/ads.html">
            <button id="stare" onmouseover='action1("stare")' onmouseout='action2("stare")'>К объявлениям
            </button>
        </a>
        </td>
    </tr>
</table>
</body>
</html>
