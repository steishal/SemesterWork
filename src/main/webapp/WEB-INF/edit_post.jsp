<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/edit_post_styles.css">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Редактирование поста</title>
</head>
<body>

<div class="form-container">
    <h2>Редактировать пост</h2>
    <form action="${pageContext.request.contextPath}/editPost" method="POST" enctype="multipart/form-data">
        <div class="form-group">
        <input type="hidden" id="id" name="id" value="${post.getId()}" required>
        </div>
        <div class="form-group">
            <label for="content">Контент:</label>
            <textarea id="content" name="content" rows="4" required>${post.getContent()}</textarea>
        </div>
        <div class="form-group">
            <label for="categoryId">ID категории:</label>
            <input type="number" id="categoryId" name="categoryId" value="${post.getCategoryId()}" required>
        </div>
        <div class="form-group">
            <label for="image0">Изображение 1:</label>
            <input type="file" id="image0" name="image0">
        </div>
        <div class="form-group">
            <label for="image1">Изображение 2:</label>
            <input type="file" id="image1" name="image1">
        </div>
        <div class="form-group">
            <label for="image2">Изображение 3:</label>
            <input type="file" id="image2" name="image2">
        </div>
        <div class="form-group">
            <button type="submit">Сохранить изменения</button>
        </div>
    </form>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        document.querySelector('form').addEventListener('submit', function (event) {
            const files = document.querySelectorAll('input[type="file"]');
            const maxSize = 50 * 1024 * 1024; // 50 MB

            for (let fileInput of files) {
                if (fileInput.files[0] && fileInput.files[0].size > maxSize) {
                    alert(`Файл ${fileInput.files[0].name} превышает размер 50 MB.`);
                    event.preventDefault();
                    return;
                }
            }
        });
    });
</script>

</body>
</html>

