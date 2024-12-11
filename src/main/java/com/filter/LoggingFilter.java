package com.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

@WebFilter("/*")
public class LoggingFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();
        String clientIP = request.getRemoteAddr();

        logger.info("Метод: {}, URI: {}, Клиент: {}", method, uri, clientIP);

        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = httpRequest.getHeader(headerName);

            if ("Authorization".equalsIgnoreCase(headerName)) {
                logger.debug("Заголовок: {} = {}", headerName, "[REDACTED]");
            } else {
                logger.debug("Заголовок: {} = {}", headerName, headerValue);
            }
        }

        long startTime = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Ошибка при обработке запроса: {}, URI: {}", e.getMessage(), uri, e);
            throw e;
        }
        long duration = System.currentTimeMillis() - startTime;

        logger.info("Запрос к {} обработан за {} мс", uri, duration);
    }

    @Override
    public void destroy() {

    }
}



