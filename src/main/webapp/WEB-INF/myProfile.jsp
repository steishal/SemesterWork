<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Главная страница</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <!-- Включаем стили -->
    <c:import url="/WEB-INF/styles.jsp" />
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f3f6f9;
            margin: 0;
            padding: 0;
        }

        .content {
            max-width: 800px;
            margin: 20px auto;
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }

        .profile-card {
            text-align: center;
            margin-bottom: 30px;
        }

        .profile-card h1 {
            font-size: 24px;
            margin-bottom: 10px;
            color: #333;
        }

        .profile-card p {
            margin: 5px 0;
            color: #555;
        }

        .profile-card a {
            color: #4680c2;
            text-decoration: none;
        }

        .profile-card a:hover {
            text-decoration: underline;
        }

        .subscriptions, .followers {
            display: inline-block;
            width: 45%;
            margin: 10px 2%;
            text-align: center;
            background: #f7f9fc;
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .subscriptions h2, .followers h2 {
            font-size: 16px;
            margin-bottom: 8px;
            color: #333;
        }

        .subscriptions p, .followers p {
            font-size: 20px;
            font-weight: bold;
            color: #4680c2;
        }

        .create-post-btn {
            display: block;
            margin: 20px auto;
            padding: 10px 20px;
            background-color: #4680c2;
            color: #fff;
            text-align: center;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
            width: 50%;
        }

        .create-post-btn:hover {
            background-color: #326fa2;
        }

        .post-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 20px;
            padding: 10px;
            border-top: 1px solid #e7e7e7;
        }

        .post-actions button {
            border: none;
            background: none;
            cursor: pointer;
        }

        .post-actions i {
            font-size: 18px;
            color: #555;
        }

        .post-actions i:hover {
            color: #4680c2;
        }
    </style>

</head>
<body>

<!-- Включаем боковую панель -->
<c:import url="/WEB-INF/sidebar.jsp" />

<!-- Основной контент -->
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
