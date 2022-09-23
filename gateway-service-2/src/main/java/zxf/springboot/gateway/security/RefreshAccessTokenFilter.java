package zxf.springboot.gateway.security;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;

public class RefreshAccessTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityUtils.isCurrentAccessTokenExpired()) {
            SecurityUtils.setCurrentAccessToken(SecurityUtils.getCurrentRefreshToken() + "-" + ZonedDateTime.now());
        }

        filterChain.doFilter(request, response);
    }
}
