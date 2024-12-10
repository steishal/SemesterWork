<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form action="${pageContext.request.contextPath}/createPost" method="POST" enctype="multipart/form-data">
    <div>
        <label for="content">Контент:</label><br>
        <textarea id="content" name="content" rows="4" cols="50" required></textarea>
    </div>
    <br>
    <div>
        <label for="categoryId">ID категории:</label><br>
        <input type="number" id="categoryId" name="categoryId" required>
    </div>
    <br>
    <div>
        <label for="image0">Изображение 1:</label><br>
        <input type="file" id="image0" name="image0">
    </div>
    <div>
        <label for="image1">Изображение 2:</label><br>
        <input type="file" id="image1" name="image1">
    </div>
    <div>
        <label for="image2">Изображение 3:</label><br>
        <input type="file" id="image2" name="image2">
    </div>
    <br>
    <div>
        <input type="submit" value="Создать пост">
    </div>
</form>


