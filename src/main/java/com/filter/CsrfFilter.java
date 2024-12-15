package com.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.utils.CsrfToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@WebFilter("/*")
public class CsrfFilter extends HttpFilter {

    private static final Logger logger = LogManager.getLogger(CsrfFilter.class);
    private final CsrfToken csrfTokenService = new CsrfToken();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String uri = req.getRequestURI();
        if (uri.equals(req.getContextPath() + "/createPost") || uri.equals(req.getContextPath() + "/main") || uri.equals(req.getContextPath() + "/comments") || uri.equals(req.getContextPath() + "/follow") || uri.equals(req.getContextPath() + "/profile")) {
            chain.doFilter(req, res);
            return;
        }

        String method = req.getMethod();

        try {
            if ("GET".equalsIgnoreCase(method)) {
                handleGetRequest(req);
            } else if ("POST".equalsIgnoreCase(method)) {
                if (!handlePostRequest(req)) {
                    logger.warn("Несовпадение CSRF-токена для URI: {}", req.getRequestURI());
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.getWriter().println("CSRF token mismatch");
                    return;
                }
            }
        } catch (Exception e) {
            throw e;
        }

        chain.doFilter(req, res);
    }

    private void handleGetRequest(HttpServletRequest req) {
        String csrfToken = csrfTokenService.generateToken();
        csrfTokenService.storeTokenInSession(req.getSession(), csrfToken);
        req.setAttribute("csrf_token", csrfToken);
    }

    private boolean handlePostRequest(HttpServletRequest req) {
        String expectedToken = csrfTokenService.getTokenFromSession(req.getSession());
        String actualToken = req.getParameter("csrf_token");
        return csrfTokenService.isTokenValid(expectedToken, actualToken);
    }
}
