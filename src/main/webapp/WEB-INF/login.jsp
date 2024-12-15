<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
    <c:import url="/WEB-INF/registerlog_styles.jsp" />
</head>
<body>
<h2>Login</h2>
<form action="${pageContext.request.contextPath}/login" method="POST" novalidate>
    <input type="hidden" name="csrf_token" value="${csrf_token}">
    <label for="email">Email:</label>
    <input
            type="email"
            id="email"
            name="email"
            required
            title="Enter a valid email address.">

    <label for="password">Password:</label>
    <input
            type="password"
            id="password"
            name="password"
            required
            minlength="6"
            title="Password must be at least 6 characters long.">

    <button type="submit">Login</button>

    <div class="error">
        <%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %>
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
