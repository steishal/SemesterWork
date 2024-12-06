package com.filter;

import com.service.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;



import com.service.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Фильтр аутентификации, который применяется ко всем запросам (/*)
@WebFilter("/*")
public class AuthFilter implements Filter {
    // Пути, доступ к которым возможен только для аутентифицированных пользователей
    private static final String[] SECURED_PATHS = {"/profile"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Метод для инициализации фильтра, если это необходимо
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Приведение общего интерфейса ServletRequest к HttpServletRequest
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Получение объекта UserService из контекста приложения
        UserService userService = (UserService) req.getServletContext().getAttribute("userService");

        // Получение запрашиваемого пути
        String path = req.getServletPath();

        // Проверка, является ли запрашиваемый путь защищённым
        if (isSecuredPath(path) && !userService.isNonAnonymous(req, res)) {
            // Если пользователь не аутентифицирован, перенаправляем его на страницу входа
            res.sendRedirect(req.getContextPath() + "/signin");
            return;
        }

        // Добавление текущего пользователя в атрибуты запроса для удобства работы в сервлетах
        req.setAttribute("user", userService.getUser(req, res));

        // Передача управления следующему фильтру или сервлету
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        // Метод для освобождения ресурсов фильтра, если это необходимо
    }

    // Проверяет, входит ли указанный путь в список защищённых
    private boolean isSecuredPath(String path) {
        for (String securedPath : SECURED_PATHS) {
            // Если путь начинается с защищённого, возвращаем true
            if (path.startsWith(securedPath)) {
                return true;
            }
        }
        return false;
    }
}


