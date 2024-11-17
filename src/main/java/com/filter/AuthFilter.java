package com.filter;

import com.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebFilter("/*")
public class AuthFilter extends HttpFilter {
    private static final Set<String> SECURED_PATHS = new HashSet<>();
    private UserService userService;

    static {
        SECURED_PATHS.add("/book/create");
        // Добавьте сюда другие защищенные пути
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        userService = (UserService) getServletContext().getAttribute("userService");
        if (userService == null) {
            throw new ServletException("UserService not initialized in servlet context");
        }
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String relativePath = getRelativePath(req);

        boolean isSecuredPath = SECURED_PATHS.contains(relativePath);
        boolean isAuthenticated = userService.isNonAnonymous(req, res);

        if (isSecuredPath && !isAuthenticated) {
            res.sendRedirect(req.getContextPath() + "/signin");
            return;
        }

        if (isAuthenticated) {
            req.setAttribute("user", userService.getUser(req, res));
        }

        chain.doFilter(req, res);
    }

    private String getRelativePath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }
}

