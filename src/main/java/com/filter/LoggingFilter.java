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
        // Здесь можно добавить инициализацию фильтра, если это необходимо
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();
        String clientIP = request.getRemoteAddr();

        // Логируем начало обработки запроса
        logger.info("Метод: {}, URI: {}, Клиент: {}", method, uri, clientIP);

        // Логируем заголовки запроса (без чувствительных данных)
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = httpRequest.getHeader(headerName);
            // Исключаем чувствительные данные, такие как Authorization
            if ("Authorization".equalsIgnoreCase(headerName)) {
                logger.debug("Заголовок: {} = {}", headerName, "[REDACTED]");
            } else {
                logger.debug("Заголовок: {} = {}", headerName, headerValue);
            }
        }

        // Засекаем время выполнения запроса
        long startTime = System.currentTimeMillis();
        try {
            // Передаем запрос и ответ через фильтр
            chain.doFilter(request, response);
        } catch (Exception e) {
            // Логируем ошибку, если произошла
            logger.error("Ошибка при обработке запроса: {}, URI: {}", e.getMessage(), uri, e);
            throw e;  // Пробрасываем ошибку дальше
        }
        long duration = System.currentTimeMillis() - startTime;

        // Логируем время, затраченное на обработку запроса
        logger.info("Запрос к {} обработан за {} мс", uri, duration);
    }

    @Override
    public void destroy() {
        // Здесь можно освободить ресурсы или выполнить другие действия при уничтожении фильтра
    }
}



