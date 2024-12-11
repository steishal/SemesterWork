<%@ page import="com.models.Post" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Главная страница</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            background-color: #f9f9f9;
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

        .content {
            margin-left: 270px;
            padding: 20px;
            width: calc(100% - 270px);
        }

        .post-container {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            padding: 20px;
            margin: 20px auto;
            max-width: 600px;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .post-container:hover {
            transform: translateY(-10px);
            box-shadow: 0 8px 12px rgba(0, 0, 0, 0.15);
        }

        .post-container h3 {
            color: #333;
            font-size: 1.8em;
            margin-bottom: 10px;
        }

        .post-container p {
            font-size: 1.1em;
            margin: 10px 0;
            line-height: 1.6;
        }

        .post-container img {
            max-width: 100%;
            height: auto;
            border-radius: 8px;
            margin: 10px 0;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .no-posts {
            text-align: center;
            font-size: 1.3em;
            color: #888;
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

<div class="content">
    <h1>Главная страница — Посты</h1>
    <div>
        <%
            List<Post> posts = (List<com.models.Post>) request.getAttribute("posts");
            if (posts != null && !posts.isEmpty()) {
                for (com.models.Post post : posts) {
        %>
        <div class="post-container">
            <h3>Пост № <%= post.getId() %></h3>
            <p><strong>Контент:</strong> <%= post.getContent() %></p>
            <p><strong>Дата создания:</strong> <%= post.getCreateDate() != null ? post.getCreateDate() : "Не указана" %></p>
            <div>
                <%
                    if (post.getImages() != null && !post.getImages().isEmpty()) {
                        for (String imgPath : post.getImages()) {
                %>
                <img src="<%= request.getContextPath() + "/uploads/" + post.getId() + "/" + imgPath.substring(imgPath.lastIndexOf('/') + 1) %>" alt="Изображение">
                <%
                    }
                } else {
                %>
                <p>Изображений нет</p>
                <%
                    }
                %>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <p class="no-posts">Посты не найдены.</p>
        <%
            }
        %>
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




