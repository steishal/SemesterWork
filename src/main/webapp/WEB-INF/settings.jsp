<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ru">
<head>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/settings_styles.css">
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Настройки конфиденциальности</title>
  <script>
    function validatePasswords() {
      const password = document.getElementById('password').value;
      const confirmPassword = document.getElementById('confirmPassword').value;
      if (password !== confirmPassword) {
        alert('Пароли не совпадают!');
        return false;
      }
      return true;
    }
  </script>
</head>
<body>
<div class="content">
  <h2>Настройки конфиденциальности</h2>
  <form action="<c:url value='/settings' />" method="post" class="settings-form">
    <input type="hidden" name="action" value="updateEmail" />
    <div class="form-field">
      <label for="email">Новый email:</label>
      <input type="email" id="email" name="email" required />
    </div>
    <button type="submit" class="create-post-btn">Изменить email</button>
  </form>
  <form action="<c:url value='/settings' />" method="post" onsubmit="return validatePasswords()" class="settings-form">
    <input type="hidden" name="action" value="updatePassword" />
    <div class="form-field">
      <label for="password">Новый пароль:</label>
      <input type="password" id="password" name="password" required minlength="8" />
    </div>
    <div class="form-field">
      <label for="confirmPassword">Повторите пароль:</label>
      <input type="password" id="confirmPassword" name="confirmPassword" required minlength="8" />
    </div>
    <button type="submit" class="create-post-btn">Изменить пароль</button>
  </form>
  <form action="<c:url value='/deleteProfile' />" method="post">
    <input type="hidden" name="action" value="deleteProfile" />
    <button type="submit" class="delete-btn" onclick="return confirm('Вы уверены, что хотите удалить профиль?')">Удалить профиль</button>
  </form>
  <a href="<c:url value='/profile?id=${sessionScope.userId}' />" class="back-to-profile-btn">Вернуться в профиль</a>
</div>
</body>
</html>