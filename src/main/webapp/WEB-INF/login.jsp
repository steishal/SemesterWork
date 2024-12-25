<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registerlog_styles.css">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Страница входа</title>
</head>
<body>
<h2>Вход в систему</h2>
<form action="${pageContext.request.contextPath}/login" method="POST" novalidate>
    <label for="email">Почта:</label>
    <input
            type="email"
            id="email"
            name="email"
            required
            title="Enter a valid email address.">

    <label for="password">Пароль:</label>
    <input
            type="password"
            id="password"
            name="password"
            required
            minlength="6"
            title="Password must be at least 6 characters long.">

    <button type="submit">Войти</button>

    <div class="error">
        <%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %>
    </div>
</form>
<div class="reg_button_div"><a href="<c:url value='/register' />" class="reg_button">Зарегистрироваться</a></div>
<script>
    document.querySelector('form').addEventListener('submit', function(event) {
        const form = event.target;
        if (!form.checkValidity()) {
            event.preventDefault();
            alert('Please fill out the form correctly before submitting.');
        }
    });
</script>
</body>
</html>
