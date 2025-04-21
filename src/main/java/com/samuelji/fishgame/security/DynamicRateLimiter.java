package com.samuelji.fishgame.security;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class DynamicRateLimiter implements Filter {

    private static final long MAX_REQUESTS_NORMAL = 1000; // 正常流量下的请求限制
    private static final long MAX_REQUESTS_HIGH_LOAD = 10; // 高负载下的请求限制

    private long requestCount = 0;

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        double systemLoad = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();

        long maxRequests = systemLoad > 2.0 ? MAX_REQUESTS_HIGH_LOAD : MAX_REQUESTS_NORMAL;

        synchronized (this) {
            requestCount++;
            if (requestCount > maxRequests) {
                ((HttpServletResponse) response).setStatus(429);
                response.getWriter().write("{\"code\": 429, \"msg\": \"Too many requests\"}");
                return;
            }
        }

        chain.doFilter(request, response);

        synchronized (this) {
            requestCount--;
        }
    }
}
