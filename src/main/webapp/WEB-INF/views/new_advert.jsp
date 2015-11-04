<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="static morgun.dev.tackboard.BoardController.userMap" %>
<%@ page import="morgun.dev.tackboard.Advert" %>
<% boolean anon = userMap.get(session.getId()).equals("anonymous"); %>
<%
    boolean edit = false;
    Advert advert = new Advert();
    if (!session.getAttribute("url").equals("no_url")) {
        advert = (Advert) session.getAttribute("advert");
        edit = true;
        session.setAttribute("advertID",advert.getId());
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>
    <title>Новое объявление</title>
    <style type="text/css">
        @import url(<c:url value="/resources/style/tackboardLoginStyle.css"/>);
    </style>
    <script src="/resources/script/buttons.js" type="text/javascript"></script>
</head>
<body background='<c:url value="/resources/images/Fence.jpg"/>'>
<div id="loginPanel">
    <%=userMap.get(session.getId())%>
</div>
<% if(!anon){ %>
<div id="logoutButton"><a href="/logout.html">Выйти</a></div>
<% } %>
<% try { %>
<% if(session.getAttribute("errortype").equals("invalidheader")) { %>
<script type="text/javascript">alert('Соблюдайте параметры ввода заголовка\nон должен содержать только буквы, цифры и пробелы\nи быть не короче 4 и не длинне 30 знаков');</script>
<% } %>
<% if(session.getAttribute("errortype").equals("invalidtext")) { %>
<script type="text/javascript">alert('Соблюдайте параметры ввода текста объявления\nон должен содержать только буквы, цифры и знаки препинания,\nбыть длиннее 3 и короче 511 знаков');</script>
<% } %>
<% } catch (Exception ex) {}  %>
<table id="header" width="100%">
    <tr id="tdCenter">
        <td width="35%" height="">Меню<br><br><br><br><br></td>
        <td valign="top">Добавить объявление</td>
    </tr>
    <tr>
        <td valign="top">
            <div id="navcontainer">
                <ul>
                    <li><a<% if (edit) { %> href="/new_advert.html" <% } %> >Добавить объявление</a></li>
                    <li><a <% if(anon) { %> href="/login.html" <% } %> >Авторизация</a></li>
                    <li><a <% if (anon) { %> href="/register" <% } %> >Регистрация</a></li>
                </ul>
            </div>
        </td>
        <td>
            <table width="100%">
                <tr>
                    <td height="70px" valign="top" align="left">
                        <p>Выберите рубрику</p>
                        <select name="topic" form="newAdvert">
                            <option value="sale">продажа</option>
                            <option value="buy">покупка</option>
                            <option value="rent">аренда</option>
                            <option value="service">услуги</option>
                            <option value="dating">знакомства</option>
                            <option value="etc">разное</option>
                        </select>
                    </td>
                    <td>
                        <p>Заголовок объявления</p>
                        <input name="header" form="newAdvert" required maxlength="30" type="text" pattern="^[a-zA-ZА-Яа-яЁё0-9\s]+$" title="Только буквы и цифры"
                                <% if (edit) { %>
                               value="<%=advert.getHeader()%>"
                                <% } %>
                                />
                    </td>
                </tr>
                <tr>
                    <td colspan="2" valign="center" align="left">
                        <br><br><br>
                        <textarea name="text" rows="10" cols="100" form="newAdvert" maxlength="511"
                                <% if (!edit) { %>
                                  placeholder="Текст вашего объявления" <% } %> required<% if (edit) { %>
                            ><%=advert.getText()%></textarea> <%--текст объявления--%>
                            <% } else { %> ></textarea> <% }%>
                    </td>
                </tr>
            </table>
            <% if(edit){ %>
            <% session.setAttribute("pictureName",advert.getPictureLink()); %>
            <img src="/picture" width="200" height="150" align="right"/>
            <p align="left">Можно выбрать другое фото для вашего объявления</p>
            <input type="file" name="adsPicture" align="left" form="newAdvert">
            <br><br><br>
            <% } else { %>
            <p align="left">Выберите фото для вашего объявления</p>
            <input type="file" name="adsPicture" align="left" form="newAdvert">
            <br><br><br>
            <% } %>
        </td>
    </tr>
    <tr>
        <td id="style2" height="100px" valign="center">
            <a href="/ads.html">
                <button id="login" onmouseover='action1("login")' onmouseout='action2("login")'>К объявлениям</button>
            </a>
        </td>
        <td align="center">
            <% if(!edit) { %>
            <form id="newAdvert" action="/send_advert.html" method="post" enctype="multipart/form-data">
                <input type="submit" onclick="return confirm('Вы действительно хотите\nподать объявление?')" value="Подать объявление">
            </form>
            <% } else { %>
            <form id="newAdvert" action="/accept.html" method="post" enctype="multipart/form-data">
                <input type="submit" value="Принять изменения" onclick="return confirm('Вы действительно хотите\nпринять изменения?')">
            </form>
            <% } %>
        </td>
    </tr>
</table>
<% session.setAttribute("errortype",null); %>
</body>
</html>
