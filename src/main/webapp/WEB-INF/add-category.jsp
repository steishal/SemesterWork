<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Управление категориями</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/categories_styles.css">
</head>
<body>

<div class="form-container">
  <h2>Добавить новую категорию</h2>
  <form action="${pageContext.request.contextPath}/admin" method="post">
    <div class="form-group">
      <label for="name">Название категории:</label>
      <input type="text" id="name" name="name" required placeholder="Введите название категории">
    </div>
    <div class="form-group">
      <button type="submit">Добавить</button>
    </div>
  </form>
  <c:if test="${not empty message}">
    <p class="error-message">${message}</p>
  </c:if>
</div>

<div class="form-container">
  <h2>Список категорий</h2>
  <table class="categories-table">
    <thead>
    <tr>
      <th>ID</th>
      <th>Название</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="category" items="${categories}">
      <tr>
        <td>${category.categoryId}</td>
        <td>${category.name}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>

</body>
</html>


