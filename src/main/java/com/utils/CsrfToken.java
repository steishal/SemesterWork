package com.utils;

import javax.servlet.http.HttpSession;
import java.util.UUID;

// Сервис для управления CSRF-токенами
public class CsrfToken {

    private static final String CSRF_SESSION_ATTRIBUTE = "csrf_token";

    // Генерация уникального CSRF-токена
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    // Сохранение токена в сессии
    public void storeTokenInSession(HttpSession session, String token) {
        session.setAttribute(CSRF_SESSION_ATTRIBUTE, token);
    }

    // Извлечение токена из сессии
    public String getTokenFromSession(HttpSession session) {
        return (String) session.getAttribute(CSRF_SESSION_ATTRIBUTE);
    }

    // Проверка токена
    public boolean isTokenValid(String expectedToken, String actualToken) {
        return expectedToken != null && expectedToken.equals(actualToken);
    }
}
