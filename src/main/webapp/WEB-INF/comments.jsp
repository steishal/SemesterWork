<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Comments</title>
    <style>
        /* Стили для комментариев в стиле ВК */
        .comment-section {
            max-width: 600px;
            margin: 0 auto;
        }

        .comment {
            display: flex;
            align-items: flex-start;
            margin-bottom: 15px;
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }

        .comment-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background-color: #ccc;
            margin-right: 10px;
            flex-shrink: 0;
        }

        .comment-content {
            flex: 1;
        }

        .comment-author {
            font-weight: bold;
            margin-bottom: 5px;
        }

        .comment-text {
            margin-bottom: 5px;
        }

        .comment-time {
            font-size: 0.9em;
            color: #888;
        }

        .comment-form {
            margin-top: 20px;
        }

        .comment-form textarea {
            width: 100%;
            height: 60px;
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            resize: none;
        }

        .comment-form button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .comment-form button:hover {
            background-color: #45a049;
        }

        .post-section {
            margin-bottom: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #f9f9f9;
        }
        .post-header {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
        }
        .post-author-avatar {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            margin-right: 10px;
        }
        .post-content img {
            max-width: 100%;
            border-radius: 5px;
            margin-top: 10px;
        }

    </style>
</head>
<body>
<div class="post-section">
    <div class="post-header">
        <img src="Avatar" alt="Avatar" class="post-author-avatar">
        <div>
            <h3>${postAuthor.username}</h3>
            <p>${post.getCreateDate()}</p>
        </div>
    </div>
    <div class="post-content">
        <p>${post.content}</p>
        <c:if test="${not empty post.images}">
            <c:forEach var="image" items="${post.images}">
                <img src="${pageContext.request.contextPath}/uploads/${post.id}/${image}" alt="Image" class="post-image">
            </c:forEach>
        </c:if>
    </div>
</div>

<div class="comment-section">
    <h2>Комментарии</h2>

    <!-- Список комментариев -->
    <c:forEach var="comment" items="${comments}">
        <div class="comment">
            <div class="comment-avatar"></div>
            <div class="comment-content">
                <div class="comment-author">${comment.authorName}</div>
                <div class="comment-text">${comment.content}</div>
                <div class="comment-time">${comment.createDate}</div>
            </div>
        </div>
    </c:forEach>

    <!-- Форма добавления нового комментария -->
    <form class="comment-form" action="${pageContext.request.contextPath}/comments" method="POST">
        <input type="hidden" name="postId" value="${postId}">
        <input type="hidden" name="userId" value="${userId}">
        <label>
            <textarea name="content" placeholder="Написать комментарий..."></textarea>
        </label>
        <button type="submit">Отправить</button>
    </form>
</div>
</body>
</html>

