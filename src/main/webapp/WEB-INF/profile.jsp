<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            background-color: #f7f7f7;
        }

        .sidebar {
            width: 250px;
            height: 100vh;
            background-color: #4a90e2;
            color: white;
            padding: 20px;
            position: fixed;
            top: 0;
            left: 0;
            box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
        }

        .sidebar h2 {
            text-align: center;
            margin-bottom: 40px;
            font-size: 1.8em;
        }

        .sidebar a {
            display: block;
            color: white;
            padding: 12px;
            text-decoration: none;
            font-size: 1.2em;
            margin: 10px 0;
            border-radius: 5px;
            transition: background-color 0.3s;
        }

        .sidebar a:hover {
            background-color: #2c7dc6;
        }

        .open-btn {
            position: fixed;
            top: 20px;
            left: 20px;
            background-color: #4a90e2;
            color: white;
            padding: 10px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1.5em;
            transition: background-color 0.3s ease;
            z-index: 100;
        }

        .open-btn:hover {
            background-color: #2c7dc6;
        }

        .content {
            margin-left: 270px;
            padding: 20px;
            width: calc(100% - 270px);
            transition: margin-left 0.3s ease;
        }

        .container {
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        h1 {
            color: #333;
            text-align: center;
            font-size: 2.5em;
            margin-bottom: 20px;
        }

        p {
            font-size: 1.2em;
            margin: 10px 0;
            color: #555;
        }

        strong {
            color: #333;
        }

        a {
            color: #0066cc;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="open-btn" onclick="toggleSidebar()">☰</div>

<div class="sidebar" id="sidebar">
    <h2>Навигация</h2>
    <a href="<c:url value='/main'/>">Главная</a>
    <a href="<c:url value='/profile'/>">Моя страница</a>
    <a href="<c:url value='/mypost'/>">Мои посты</a>
    <a href="/subscriptions">Подписки</a>
</div>

<div class="content" id="content">
    <div class="container">
        <h1>Profile of ${user.username}</h1>
        <p><strong>Email:</strong> ${user.email}</p>
        <p><strong>Phone Number:</strong> ${user.phoneNumber}</p>
        <p><strong>VK Link:</strong> <a href="${user.vkLink}" target="_blank">${user.vkLink}</a></p>
        <p><strong>Telegram Link:</strong> <a href="${user.tgLink}" target="_blank">${user.tgLink}</a></p>
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





