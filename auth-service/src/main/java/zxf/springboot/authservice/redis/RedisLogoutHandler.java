package zxf.springboot.authservice.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;
import zxf.springboot.authentication.MyAuthentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static zxf.springboot.authservice.redis.RedisSecurityContextRepository.SECURITY_CONTEXT;

@Service("redisLogoutHandler")
public class RedisLogoutHandler implements LogoutSuccessHandler {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof MyAuthentication) {
            String tokenId = ((MyAuthentication) authentication).getTokenId();
            redisTemplate.opsForHash().delete(SECURITY_CONTEXT, tokenId);
        }

        response.sendRedirect("/auth/logon");
    }
}
