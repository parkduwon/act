package com.act.ldk.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class IpWhitelistFilter extends OncePerRequestFilter {
    
    // 허용된 IP 주소 목록
    private static final List<String> ALLOWED_IPS = Arrays.asList(
        "175.196.78.15",
        "125.131.198.22", 
        "175.209.18.40",
        "127.0.0.1",      // localhost
        "0:0:0:0:0:0:0:1" // IPv6 localhost
    );
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String clientIp = getClientIp(request);
        
        // IP 확인 로그
        log.debug("접속 시도 IP: {}", clientIp);
        
        if (!isAllowedIp(clientIp)) {
            log.warn("차단된 IP 접근 시도: {}", clientIp);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * 클라이언트의 실제 IP 주소 추출
     * 프록시나 로드밸런서를 통과한 경우도 고려
     */
    private String getClientIp(HttpServletRequest request) {
        String[] headers = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
        };
        
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For의 경우 쉼표로 구분된 여러 IP가 있을 수 있음
                // 첫 번째 IP가 실제 클라이언트 IP
                return ip.split(",")[0].trim();
            }
        }
        
        return request.getRemoteAddr();
    }
    
    /**
     * IP가 허용 목록에 있는지 확인
     */
    private boolean isAllowedIp(String ip) {
        return ALLOWED_IPS.contains(ip);
    }
}