package com.filter;

import com.dao.UserDao;
import com.models.User;
import com.utils.DbException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/admin")
public class AdminFilter implements Filter {

    private UserDao userDao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userDao = (UserDao) filterConfig.getServletContext().getAttribute("userDao");
        if (userDao == null) {
            throw new ServletException("UserDao not found in ServletContext.");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId != null) {
                try {
                    User currentUser = userDao.getUserById(userId);
                    if (currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole())) {
                        chain.doFilter(request, response);
                        return;
                    }
                } catch (DbException e) {
                    throw new ServletException("Ошибка при проверке роли пользователя", e);
                }
            }
        }

        httpResponse.sendRedirect(httpRequest.getContextPath() + "/access-denied");
    }

    @Override
    public void destroy() {
    }
}

