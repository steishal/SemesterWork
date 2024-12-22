<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html lang="ru">
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/edit_profile_styles.css">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Редактирование профиля</title>
</head>
<body>
<div class="content">
    <h2>Редактирование профиля</h2>
    <form action="<c:url value='/editProfile' />" method="post" class="profile-card">
        <div class="form-field">
            <label for="username">Имя пользователя:</label>
            <input type="text" id="username" name="username" value="${user.username}" required>
        </div>

        <div class="form-field">
            <label for="phoneNumber">Телефон:</label>
            <input type="text" id="phoneNumber" name="phoneNumber" value="${user.phoneNumber}">
        </div>

        <div class="form-field">
            <label for="vkLink">Ссылка VK:</label>
            <input type="url" id="vkLink" name="vkLink" value="${user.vkLink}">
        </div>

        <div class="form-field">
            <label for="tgLink">Ссылка Telegram:</label>
            <input type="url" id="tgLink" name="tgLink" value="${user.tgLink}">
        </div>

        <button class="create-post-btn" type="submit">Сохранить изменения</button>
    </form>
</div>
</body>
</html>

