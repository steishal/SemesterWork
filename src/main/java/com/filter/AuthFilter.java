package com.filter;

import com.service.UserService;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final Logger logger = Logger.getLogger(AuthFilter.class.getName());
    private static final Set<String> SECURED_PATHS = new HashSet<>();
    private UserService userService;
    static {
        SECURED_PATHS.add("/profile");
        SECURED_PATHS.add("/createPost");
        SECURED_PATHS.add("/admin");
        SECURED_PATHS.add("/comments");
        SECURED_PATHS.add("/editPost");
        SECURED_PATHS.add("/editProfile");
        SECURED_PATHS.add("/main");
        SECURED_PATHS.add("/settings");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext context = filterConfig.getServletContext();
        this.userService = (UserService) context.getAttribute("userService");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        request.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();

        if (isSecuredPath(path) && !userService.isNonAnonymous(req)) {
            logger.warning("Неавторизованный доступ к защищённому пути: " + path);
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        chain.doFilter(req, res);
    }

    private boolean isSecuredPath(String path) {
        return SECURED_PATHS.contains(path);
    }

    @Override
    public void destroy() {
    }
}




