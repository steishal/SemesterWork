<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Подтверждение пароля</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/confirm_password_styles.css">
</head>
<body>
<div class="container">
    <h2>Для доступа к настройкам конфиденциальности введите свой пароль</h2>
    <form action="<c:url value='/confirmPassword' />" method="post" class="confirm-form">
        <div class="form-field">
            <label for="password">Пароль:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit" class="confirm-btn">Подтвердить</button>
    </form>
    <c:if test="${param.error != null}">
        <p class="error-message">Неверный пароль, попробуйте снова.</p>
    </c:if>
</div>
</body>
</html>


