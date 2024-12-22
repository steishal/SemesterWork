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
        <div class="profile-header">
            <div class="profile-actions">
                <i class="fas fa-cog" onclick="toggleActionsMenu()"></i>
                <div id="actions-menu" class="actions-menu" style="display: none;">
                    <a href="<c:url value='/editProfile' />">Редактировать профиль</a>
                    <a href="<c:url value='/confirmPassword' />">Настройки конфиденциальности</a>
                </div>
            </div>
        </div>
        <h1>${user.username}</h1>
        <c:if test="${not empty user.email}">
            <p><strong>Email:</strong> ${user.email}</p>
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
    <div class="subscriptions">
        <h2>Подписки</h2>
        <p>${subscriptionCount}</p>
    </div>

    <div class="followers">
        <h2>Подписчики</h2>
        <p>${followerCount}</p>
    </div>
    <div class="create-post-btn" onclick="location.href='<c:url value='/createPost' />'">Создать пост</div>
</div>

<script>
    function toggleActionsMenu() {
        var actionsMenu = document.getElementById('actions-menu');
        if (actionsMenu.style.display === 'none' || actionsMenu.style.display === '') {
            actionsMenu.style.display = 'block';
        } else {
            actionsMenu.style.display = 'none';
        }
    }
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
