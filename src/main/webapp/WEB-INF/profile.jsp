<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Profile</title>
</head>
<body>
<h1>Profile of ${user.username}</h1>
<p><strong>Email:</strong> ${user.email}</p>
<p><strong>Phone Number:</strong> ${user.phoneNumber}</p>
<p><strong>VK Link:</strong> <a href="${user.vkLink}" target="_blank">${user.vkLink}</a></p>
<p><strong>Telegram Link:</strong> <a href="${user.tgLink}" target="_blank">${user.tgLink}</a></p>
</body>
</html>
