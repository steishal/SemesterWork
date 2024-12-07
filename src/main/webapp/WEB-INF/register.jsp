<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Register</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 20px auto;
      max-width: 600px;
      padding: 10px;
      background-color: #f9f9f9;
      border-radius: 8px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }
    h2 {
      text-align: center;
      color: #333;
    }
    form {
      display: flex;
      flex-direction: column;
    }
    label {
      margin-top: 10px;
      font-weight: bold;
    }
    input, button {
      padding: 8px;
      margin-top: 5px;
      font-size: 14px;
    }
    input:invalid {
      border: 1px solid red;
    }
    input:valid {
      border: 1px solid green;
    }
    button {
      background-color: #4CAF50;
      color: white;
      border: none;
      margin-top: 15px;
      cursor: pointer;
    }
    button:hover {
      background-color: #45a049;
    }
    .error {
      color: red;
      margin-top: 10px;
    }
  </style>
</head>
<body>
<h2>Register</h2>
<form action="${pageContext.request.contextPath}/register" method="POST" novalidate>
  <input type="hidden" name="csrf_token" value="${csrf_token}">
  <!-- Username -->
  <label for="username">Username:</label>
  <input
          type="text"
          id="username"
          name="username"
          required
          minlength="3"
          maxlength="20"
          pattern="^[a-zA-Z0-9_]+$"
          title="Username must be 3-20 characters long and contain only letters, numbers, and underscores.">

  <!-- Password -->
  <label for="password">Password:</label>
  <input
          type="password"
          id="password"
          name="password"
          required
          minlength="6"
          title="Password must be at least 6 characters long.">

  <!-- Email -->
  <label for="email">Email:</label>
  <input
          type="email"
          id="email"
          name="email"
          required
          title="Enter a valid email address.">

  <!-- Phone -->
  <label for="phone">Phone Number:</label>
  <input
          type="tel"
          id="phone"
          name="phone"
          required
          pattern="\+?[0-9]{10,15}"
          title="Enter a valid phone number with 10-15 digits. You can start with '+'.">

  <!-- VK Link -->
  <label for="vkLink">VK Link:</label>
  <input
          type="url"
          id="vkLink"
          name="vkLink"
          pattern="https?://.*"
          title="Enter a valid URL starting with http:// or https://">

  <!-- Telegram Link -->
  <label for="tgLink">Telegram Link:</label>
  <input
          type="url"
          id="tgLink"
          name="tgLink"
          pattern="https?://.*"
          title="Enter a valid URL starting with http:// or https://">

  <!-- Submit Button -->
  <button type="submit">Register</button>

  <!-- Error Message (if any) -->
  <div class="error">
    <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
  </div>
</form>

<!-- Script for client-side validation -->
<script>
  document.querySelector('form').addEventListener('submit', function(event) {
    const form = event.target;
    if (!form.checkValidity()) {
      event.preventDefault(); // Останавливает отправку формы, если есть ошибки
      alert('Please fill out the form correctly before submitting.');
    }
  });
</script>
</body>
</html>

