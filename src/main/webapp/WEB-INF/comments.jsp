<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/comments.css">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Comments</title>
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

