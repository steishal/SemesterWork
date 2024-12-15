package com.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final Logger logger = Logger.getLogger(AuthFilter.class.getName());
    private static final Set<String> SECURED_PATHS = new HashSet<>();

    static {
        SECURED_PATHS.add("/profile");
        SECURED_PATHS.add("/createPost");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        request.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);

        // Проверяем наличие userId в сессии
        Object userId = (session != null) ? session.getAttribute("userId") : null;

        // Если защищенный путь и пользователя нет в сессии, перенаправляем на страницу входа
        if (isSecuredPath(path) && userId == null) {
            logger.warning("Неавторизованный доступ к защищённому пути: " + path);
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
    }

    private boolean isSecuredPath(String path) {
        return SECURED_PATHS.stream().anyMatch(path::startsWith);
    }
}




