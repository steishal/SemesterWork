<%@ page import="com.models.Post" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Главная страница</title>
    <style>
        .post-container {
            border: 1px solid #ddd;
            padding: 10px;
            margin: 10px;
            max-width: 400px;
            box-shadow: 2px 2px 5px #aaa;
        }
        img {
            max-width: 100%;
            height: auto;
            margin: 5px 0;
        }
    </style>
</head>
<body>
<h1>Главная страница — Посты</h1>
<div>
    <%
        List<Post> posts = (List<com.models.Post>) request.getAttribute("posts");
        if (posts != null && !posts.isEmpty()) {
            for (com.models.Post post : posts) {
    %>
    <div class="post-container">
        <h3>Пост № <%= post.getId() %></h3>
        <p>Контент: <%= post.getContent() %></p>
        <p>Дата создания: <%= post.getCreateDate() != null ? post.getCreateDate() : "Не указана" %></p>
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
    <p>Посты не найдены.</p>
    <%
        }
    %>
</div>
</body>
</html>


