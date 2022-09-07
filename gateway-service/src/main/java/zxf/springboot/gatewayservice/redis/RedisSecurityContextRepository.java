package zxf.springboot.gatewayservice.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import zxf.springboot.authentication.MyAuthentication;
import zxf.springboot.gatewayservice.security.SecurityUtils;
import org.apache.logging.log4j.util.Strings;

@Service
public class RedisSecurityContextRepository implements ServerSecurityContextRepository {
    public static String SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    @Autowired
    private ReactiveRedisTemplate reactiveRedisTemplate;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        if (!(context.getAuthentication() instanceof MyAuthentication)) {
            return Mono.empty();
        }

        MyAuthentication myAuthentication = (MyAuthentication) context.getAuthentication();
        if (!myAuthentication.getNeedSave()) {
            return Mono.empty();
        }

        String tokenId = myAuthentication.getTokenId();
        return reactiveRedisTemplate.opsForHash().put(SECURITY_CONTEXT, tokenId, context);
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String tokenId = SecurityUtils.getTokenIdFromRequest(exchange.getRequest());
        if (Strings.isEmpty(tokenId)) {
            return Mono.empty();
        }

        return reactiveRedisTemplate.opsForHash().get(SECURITY_CONTEXT, tokenId)
                .defaultIfEmpty(SecurityContextHolder.createEmptyContext());
    }
}
