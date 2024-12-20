<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="open-btn" onclick="toggleSidebar()">☰</div>
<div class="sidebar" id="sidebar">
  <h2>Навигация</h2>
  <a href="<c:url value='/profile?id=${sessionScope.userId}'/>">Моя страница</a>
  <a href="<c:url value='/main'/>">Главная</a>
  <a href="<c:url value='/mypost'/>">Мои посты</a>
  <a href="<c:url value='/submit'/>">Подписки</a>
</div>

