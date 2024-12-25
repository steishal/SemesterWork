<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registerlog_styles.css">
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Регистрация</title>
</head>
<body>
<h2>Регистрация</h2>
<form action="${pageContext.request.contextPath}/register" method="POST" novalidate>
  <label for="username">Имя:</label>
  <input
          type="text"
          id="username"
          name="username"
          required
          minlength="3"
          maxlength="40"
          pattern="^[a-zA-Zа-яА-ЯёЁ0-9_ ]+$"
          title="Username must be 3-20 characters long and contain only letters, numbers, and underscores.">

  <label for="password">Пароль:</label>
  <input
          type="password"
          id="password"
          name="password"
          required
          minlength="6"
          title="Password must be at least 6 characters long.">

  <label for="email">Почта:</label>
  <input
          type="email"
          id="email"
          name="email"
          required
          title="Enter a valid email address.">

  <label for="phone">Номер телефона:</label>
  <input
          type="tel"
          id="phone"
          name="phone"
          required
          pattern="\+?[0-9]{10,15}"
          title="Enter a valid phone number with 10-15 digits. You can start with '+'.">

  <label for="vkLink">Ссылка на Вконтакте:</label>
  <input
          type="url"
          id="vkLink"
          name="vkLink"
          pattern="https?://.*"
          title="Enter a valid URL starting with http:// or https://">

  <label for="tgLink">Ссылка на телеграм:</label>
  <input
          type="url"
          id="tgLink"
          name="tgLink"
          pattern="https?://.*"
          title="Enter a valid URL starting with http:// or https://">

  <button type="submit">Зарегистрироваться</button>

  <div class="error">
    <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
  </div>
</form>

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
