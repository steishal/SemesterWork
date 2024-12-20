<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.models.Post" %>
<%@ page import="com.models.User" %>
<%@ page import="java.util.List" %>

<html lang="ru">
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Главная страница</title>
</head>
<body>
<c:import url="/WEB-INF/sidebar.jsp" />
<div class="content">
    <h1>Главная</h1>
    <div>
        <%
            List<Post> posts = (List<Post>) request.getAttribute("posts");
            if (posts != null && !posts.isEmpty()) {
                for (Post post : posts) {
        %>
        <div class="post-container">
            <div class="post-header">
                <a href="<%= request.getAttribute("authorProfileUrl" + post.getId()) %>" class="post-author-name"><%= request.getAttribute("authorName" + post.getId()) %></a>
                <% if (request.getAttribute("userId").equals(post.getUserId())) { %>
                <div class="post-options">
                    <div class="options-wrapper">
                        <button class="options-button">⋮</button>
                        <div class="options-menu">
                            <a href="<%= request.getContextPath() + "/editPost?id=" + post.getId() %>">Редактировать</a>
                            <hr class="menu-divider">
                            <form action="<%= request.getContextPath() + "/deletePost" %>" method="POST">
                                <input type="hidden" name="postId" value="<%= post.getId() %>">
                                <button type="submit" class="delete-button">Удалить</button>
                            </form>
                        </div>
                    </div>
                </div>
                <% } %>
            </div>
            <p class="post-create-date"><%= post.getCreateDate() != null ? post.getCreateDate() : "Не указана" %></p>
            <div class="post-content">
                <p><%= post.getContent() %></p>
                <%
                    if (post.getImages() != null && !post.getImages().isEmpty()) {
                        for (String imgPath : post.getImages()) {
                %>
                <img src="<%= request.getContextPath() + "/uploads/" + post.getId() + "/" + imgPath.substring(imgPath.lastIndexOf('/') + 1) %>" class="post-image" alt="изображение">
                <%
                        }
                    }
                %>
            </div>
            <div class="post-actions">
                <button class="like-button" data-post-id="<%= post.getId() %>">
                    <i class="<%= (Boolean.TRUE.equals(request.getAttribute("userLiked" + post.getId()))) ? "fas fa-heart liked" : "far fa-heart" %>"></i>
                    <span class="likes-count"><%= request.getAttribute("likesCount" + post.getId()) %></span>
                </button>
                <a href="<%= request.getContextPath() + "/comments?postId=" + post.getId() %>" class="comment-button">
                    <i class="far fa-comment"></i>
                    <span class="comments-count"><%= request.getAttribute("commentsCount" + post.getId()) %></span>
                </a>
            </div>
        </div>

        <%
            }
        } else {
        %>
        <p class="no-posts">Посты не найдены</p>
        <%
            }
        %>
    </div>

</div>


<script>
    document.addEventListener('DOMContentLoaded', () => {
        document.querySelectorAll('.options-button').forEach(button => {
            button.addEventListener('click', (e) => {
                e.stopPropagation(); // Предотвращаем всплытие события
                const menu = button.nextElementSibling;
                toggleMenu(menu);
            });
        });

        // Скрываем все меню при клике вне их
        document.addEventListener('click', () => {
            document.querySelectorAll('.options-menu').forEach(menu => {
                menu.style.display = 'none';
            });
        });
    });

    function toggleMenu(menu) {
        // Скрываем другие меню
        document.querySelectorAll('.options-menu').forEach(otherMenu => {
            if (otherMenu !== menu) {
                otherMenu.style.display = 'none';
            }
        });

        // Переключаем текущее меню
        if (menu.style.display === 'block') {
            menu.style.display = 'none';
        } else {
            menu.style.display = 'block';
        }
    }
    document.addEventListener('DOMContentLoaded', () => {
        document.querySelectorAll('.like-button').forEach(button => {
            button.addEventListener('click', () => {
                const postId = button.dataset.postId;
                toggleLike(postId, button);
            });
        });
    });

    function toggleLike(postId, button) {
        const userId = <%= request.getAttribute("userId") %>;

        fetch(`${pageContext.request.contextPath}/main`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({
                postId: postId,
                userId: userId,
            }),
            credentials: 'include'
        })
            .then(response => response.json())
            .then(data => {
                const heartIcon = button.querySelector('i');
                if (data.liked) {
                    heartIcon.classList.remove('far');
                    heartIcon.classList.add('fas', 'liked');
                } else {
                    heartIcon.classList.remove('fas', 'liked');
                    heartIcon.classList.add('far');
                }
            })
            .catch(console.error);
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





