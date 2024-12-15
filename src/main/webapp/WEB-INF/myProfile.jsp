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
        <p><strong>Email:</strong> ${user.email}</p>
        <p><strong>Телефон:</strong> ${user.phoneNumber}</p>
        <p><strong>Ссылка VK:</strong> <a href="${user.vkLink}" target="_blank">${user.vkLink}</a></p>
        <p><strong>Ссылка Telegram:</strong> <a href="${user.tgLink}" target="_blank">${user.tgLink}</a></p>
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
