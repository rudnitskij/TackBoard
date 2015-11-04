<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Date" %>
<%@ page import="static morgun.dev.tackboard.DBaseHandling.getAds" %>
<%@ page import="static morgun.dev.tackboard.DBaseHandling.adsSize" %>
<%@ page import="java.util.List" %>
<%@ page import="static morgun.dev.tackboard.Advert.getTopicRus" %>
<%@ page import="static morgun.dev.tackboard.BoardController.picturesMap" %>
<%@ page import="static morgun.dev.tackboard.BoardController.userMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% Date date;
    /** string presentation of the required date for writing to the page*/
    String dateString;
    String searchField, searchValue = "";
    int adsNumber, adsPage;
    List<Object[]> ads;
    /** request URL on which the page was created */
    String url = session.getAttribute("url").toString();
    try {
        adsPage = Integer.parseInt(session.getAttribute("adsPage").toString());
    } catch (NullPointerException ex) {
        adsPage = 1;
    }
    try {
        searchField = session.getAttribute("searchfield").toString();
        searchValue = session.getAttribute("searchvalue").toString();
        adsNumber = adsSize(searchField, searchValue);
        ads = getAds(adsPage, searchField, searchValue);
    } catch (Exception exc) {
        adsNumber = adsSize();
        ads = getAds(adsPage);
    }
    /** shows if the user reading this page is logged in  */
    boolean anon = userMap.get(session.getId()).equals("anonymous");
    /** shows if the page shows only user's ads */
    boolean authorPage = url.endsWith(userMap.get(session.getId()));
    /** shows if the page is the result of searching */
    boolean searchPage = url.contains("authorsearch") || url.contains("topicsearch");
    /** shows if the page is the result of searching and if searching has found nothing */
    boolean noResults = (ads.size() == 0);
    try {
        if (url.endsWith("authorsearch")) {
            url = url + "/" + ads.get(0)[1];
        }
    } catch (IndexOutOfBoundsException e) {
    }
    try {
        if (url.endsWith("topicsearch")) {
            url = url + "/" + ads.get(0)[6];
        }
    } catch (IndexOutOfBoundsException e) {
    }
%>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>
    <title>Объявления</title>
    <style type="text/css">
        @import url(<c:url value="/resources/style/tackboardLoginStyle.css"/>);
    </style>
    <script type="text/javascript" src="/resources/script/buttons.js"></script>
</head>
<body background='<c:url value="/resources/images/Fence.jpg"/>'>
<div id="loginPanel">
    <%=userMap.get(session.getId())%>
</div>
<% if (!anon) { %>
<div id="logoutButton"><a href="/logout.html">Выйти</a></div>
<% } %>
<table id="header" width="100%">
    <tr id="tdCenter">
        <td width="25%" height="">Меню<br></td>
        <td valign="top">Объявления</td>
    </tr>
    <tr>
        <td valign="top">
            <div id="searchOptions">
                <div id="navcontainer" align="left">
                    <ul>
                        <li><a <% if (!anon) { %> href="/new_advert.html" <% } %> >Добавить объявление</a></li>
                        <li><a <% if (anon) { %> href="/login.html" <% } %> >Авторизация</a></li>
                        <li><a <% if (anon) { %> href="/register.html" <% } %> >Регистрация</a></li>
                    </ul>
                </div>
                <br><br>


                Фильтровать объявления:<br>
                <% if (!anon) { %><br>

                <p><sup><big>ТОЛЬКО МОИ ОБЪЯВЛЕНИЯ</big></sup><sub><input id="checkbox" type="checkbox"
                                                                          <% if (authorPage) { %>checked<% } %>
                                                                          onchange="checkboxProcessing('/ads','<%=userMap.get(session.getId())%>');"/></sub>
                </p> <%--Чекбокс для просмотра своих объявлений--%>
                <% } %>
                <br><br>
                <% if (!authorPage) { %>
                <div id="header2">
                    <form action="/authorsearch" method="post">Поиск по автору<br><input type="text"
                                                                                         maxlength="20"
                                                                                         pattern="^[a-zA-ZА-Яа-яЁё0-9]+$"
                                                                                         title="латинские или русские буквы и цифры"
                                                                                         name="author"/><input
                            type="submit" value="Найти"></form>
                    <%--Кнопка поиска объявлений по автору--%>
                </div>
                <% } %><br><br>
                <select name="topic" form="topicsearch">
                    <option value="sale">продажа</option>
                    <option value="buy">покупка</option>
                    <%--Выпадающий список для поиска объявлений по рубрике--%>
                    <option value="rent">аренда</option>
                    <option value="service">услуги</option>
                    <option value="dating">знакомства</option>
                    <option value="etc">разное</option>
                </select>

                <div id="header2">
                    <form action="/topicsearch" method="post" id="topicsearch"><input
                            type="submit" value="Поиск по рубрике"/></form>
                </div>
                <br><br><br>
                <% if (searchPage) { %>
                <div id="style3"><a href="/ads">
                    <button>Показать все</button>
                </a></div>
                <% } %>
            </div>


        </td>
        <td align="center" valign="top">
            <% if (!noResults) { %>
            <table id="style2" width="100%">
                <tr>
                    <td height="70px" width="25%" valign="center">
                        <a <% if (adsPage > 1) { %> href="<%=url%>" <% } %> >
                            <button>первые 10</button>
                        </a>
                    </td>
                    <td width="25%" valign="center">
                        <a <% if (adsPage != 1) { %> href="<%=url%>?adsPage=<%=(adsPage - 1)%>" <% } %> >
                            <button>предыдущие 10</button>
                        </a>
                    </td>
                    <td width="25%" valign="center">
                        <a <% if (adsPage <= (adsNumber / 10)) { %> href="<%=url%>?adsPage=<%=(adsPage + 1)%>" <% } %> >
                            <button>следующие 10</button>
                        </a>
                    </td>
                    <td valign="center">
                        <a <% if (adsPage != (adsNumber / 10) + 1) { %>
                                href="<%=url%>?adsPage=<%=((adsNumber / 10)+1)%>" <% } %> >
                            <button>последние</button>
                        </a>
                    </td>
                </tr>
            </table>
            <% } %>
            <% if (ads.size() == 0) {%>
            <% if (searchPage) { %>
            <p>Объявлений, соответствующих критериям поиска, не найдено</p>
            <% } else {%>
            <p>На данный момент в базе нет объявлений</p>
            <% } %>
            <% } else {%>
            <% if (searchPage) {
            %> <p align="center">Результаты поиска</p> <%
            }
        %>
            <table width="100%">
                <% /*начало цикла */
                    for (int i = 0; i < ads.size(); i++) { %>
                <%
                    picturesMap.put(i, (String) ads.get(i)[4]);
                %>
                <tr>
                    <td height="170px">
                        <table id="advert" width="100%">
                            <tr>
                                <td width="33%" align="left" height="40px" style="padding-left: 5px">
                                    <%=ads.get(i)[1] %> <%--автор--%>
                                </td>
                                <td align="left" width="67%">
                                    <b><%=getTopicRus((String) ads.get(i)[6])%>
                                    </b> <%--рубрика--%>
                                </td>
                            </tr>
                            <tr>
                                <td width="33%" align="left" height="40px" style="padding-left: 5px">
                                    <% date = (Date) ads.get(i)[3];%>
                                    <% dateString = String.format("%td-%tB-%tY <br> %tR", date, date, date, date);%>
                                    <%=dateString %> <%--дата и время--%>
                                </td>
                                <td align="left" width="67%">
                                    <b><%=ads.get(i)[2]%>
                                    </b> <%--заголовок--%>
                                </td>
                            </tr>
                            <tr>
                                <td width="33%" align="left" style="padding-left: 5px">
                                    <img src="/picture/<%=i%>" width="100"
                                         height="100"> <%--картинка--%><%--СДЕЛАНЫ ИЗМЕНЕНИЯ ЗДЕСЬ--%>
                                </td>
                                <td align="left" width="67%">
                                    <% try { %>
                                    <%=((String) ads.get(i)[5]).substring(0, 49)%><%=(((String) ads.get(i)[5]).length() < 49) ? "" : "..."%>
                                    <a style="color: darkorange" href="/fulladvert.html?advertID=<%=ads.get(i)[0]%>"
                                            >Смотреть&nbsp;полностью</a> <%--начало текста объявления--%>
                                    <% } catch (StringIndexOutOfBoundsException ex) { %>
                                    <%=((String) ads.get(i)[5])%><%=(((String) ads.get(i)[5]).length() < 49) ? " " : "..."%>
                                    <a style="color: darkorange" href="/fulladvert.html?advertID=<%=ads.get(i)[0]%>"
                                            >Смотреть&nbsp;полностью</a>
                                    <% } %>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <% }  /*конец цикла */ %>
            </table>
            <% if (!noResults) { %>
            <table id="style2" width="100%">
                <tr>
                    <td height="70px" width="25%" valign="center">
                        <a <% if (adsPage > 1) { %> href="<%=url%>" <% } %> >
                            <button>первые 10</button>
                        </a>
                    </td>
                    <td width="25%" valign="center">
                        <a <% if (adsPage != 1) { %> href="<%=url%>?adsPage=<%=(adsPage - 1)%>" <% } %> >
                            <button>предыдущие 10</button>
                        </a>
                    </td>
                    <td width="25%" valign="center">
                        <a <% if (adsPage <= (adsNumber / 10)) { %> href="<%=url%>?adsPage=<%=(adsPage + 1)%>" <% } %> >
                            <button>следующие 10</button>
                        </a>
                    </td>
                    <td valign="center">
                        <a <% if (adsPage != (adsNumber / 10) + 1) { %>
                                href="<%=url%>?adsPage=<%=((adsNumber / 10)+1)%>" <% } %> >
                            <button>последние</button>
                        </a>
                    </td>
                </tr>
            </table>
            <% } %>
            <% } %>
        </td>
    </tr>
</table>
</body>
</html>
