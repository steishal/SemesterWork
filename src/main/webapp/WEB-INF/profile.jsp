<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html lang="ru">
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/profile.css">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Главная страница</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
<c:import url="/WEB-INF/sidebar.jsp" />
<div class="content">
    <div class="profile-card">
        <h1>${user.username}</h1>
        <c:if test="${not empty user.email}">
            <p><strong>Почта:</strong> ${user.email}</p>
        </c:if>
        <c:if test="${not empty user.phoneNumber}">
            <p><strong>Телефон:</strong> ${user.phoneNumber}</p>
        </c:if>
        <c:if test="${not empty user.vkLink}">
            <p><strong>Ссылка VK:</strong> <a href="${user.vkLink}" target="_blank">${user.vkLink}</a></p>
        </c:if>
        <c:if test="${not empty user.tgLink}">
            <p><strong>Ссылка Telegram:</strong> <a href="${user.tgLink}" target="_blank">${user.tgLink}</a></p>
        </c:if>
    </div>
    <c:if test="${myUserId != user.id}">
        <form method="post" action="profile">
            <input type="hidden" name="targetUserId" value="${user.id}">
            <c:choose>
                <c:when test="${isFollowing}">
                    <button type="submit" name="action" value="unfollow" class="unsubscribe-btn">
                        Отписаться
                    </button>
                </c:when>
                <c:otherwise>
                    <button type="submit" name="action" value="follow" class="subscribe-btn">
                        Подписаться
                    </button>
                </c:otherwise>
            </c:choose>
        </form>
    </c:if>
    <div class="subscriptions-followers">
    <div class="subscriptions">
        <h2>Подписки</h2>
        <p>${subscriptionCount}</p>
    </div>
    <div class="followers">
        <h2>Подписчики</h2>
        <p>${followerCount}</p>
    </div>
    </div>
</div>

<script>
    function toggleSidebar() {
        var sidebar = document.getElementById('sidebar');
        var content = document.getElementById('content');
        var openBtn = document.querySelector('.open-btn');

        if (sidebar.style.left === '-300px') {
            sidebar.style.left = '0';
            content.style.marginLeft = '350px';
        } else {
            sidebar.style.left = '-300px';
            content.style.marginLeft = '0';
            openBtn.innerHTML = '☰';
        }
    }
</script>
</body>
</html>









