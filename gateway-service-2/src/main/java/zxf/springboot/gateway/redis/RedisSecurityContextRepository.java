package zxf.springboot.gateway.redis;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import zxf.springboot.authentication.MyAuthentication;
import zxf.springboot.gateway.security.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service("redisSecurityContextRepository")
public class RedisSecurityContextRepository implements SecurityContextRepository {
    public static String SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        String tokenId = SecurityUtils.getTokenIdFromRequest(requestResponseHolder.getRequest());
        if (Strings.isEmpty(tokenId)) {
            return SecurityContextHolder.createEmptyContext();
        }

        SecurityContext savedSecurityContext = (SecurityContext) redisTemplate.opsForHash().get(SECURITY_CONTEXT, tokenId);
        if (savedSecurityContext == null) {
            return SecurityContextHolder.createEmptyContext();
        }

        return savedSecurityContext;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        if (!(context.getAuthentication() instanceof MyAuthentication) || !context.getAuthentication().isAuthenticated()) {
            return;
        }

        MyAuthentication myAuthentication = (MyAuthentication) context.getAuthentication();
        if (myAuthentication.getNeedSave()) {
            String tokenId = myAuthentication.getTokenId();
            redisTemplate.opsForHash().put(SECURITY_CONTEXT, tokenId, context);
        }
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String tokenId = SecurityUtils.getTokenIdFromRequest(request);
        if (Strings.isEmpty(tokenId)) {
            return false;
        }

        return redisTemplate.opsForHash().hasKey(SECURITY_CONTEXT, tokenId);
    }
}
