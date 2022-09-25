package zxf.springboot.gatewayservice.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import zxf.springboot.authentication.MyAuthentication;
import zxf.springboot.gatewayservice.redis.RedisSecurityContextRepository;
import zxf.springboot.gatewayservice.security.SecurityUtils;
import zxf.springboot.gatewayservice.service.E2ETrustTokenService;

import java.time.ZonedDateTime;

@Component
public class E2ETrustTokenFilter extends AbstractGatewayFilterFactory<E2ETrustTokenFilter.Config> {
    @Autowired
    private E2ETrustTokenService e2ETrustTokenService;

    @Autowired
    private RedisSecurityContextRepository redisSecurityContextRepository;

    public E2ETrustTokenFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            //Pre-process for request
            ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
            return ReactiveSecurityContextHolder.getContext().defaultIfEmpty(SecurityContextHolder.createEmptyContext())
                    .map(securityContext -> {
                        if (securityContext.getAuthentication() instanceof MyAuthentication) {
                            if (SecurityUtils.isCurrentAccessTokenExpired(securityContext)) {
                                SecurityUtils.setCurrentAccessToken(securityContext, SecurityUtils.getCurrentRefreshToken(securityContext) + "-" + ZonedDateTime.now());
                            }

                            String accessToken = SecurityUtils.getCurrentAccessToken(securityContext);
                            System.out.println("E2ETrustTokenFilter::" + exchange.getRequest().getPath() + ", " + accessToken);
                            String e2eTrustToken = e2ETrustTokenService.getE2ETrustToken(accessToken);
                            builder.header(config.getE2eTokenHeader(), e2eTrustToken);
                        }
                        return securityContext;
                    })
                    .flatMap(securityContext -> redisSecurityContextRepository.save(exchange, securityContext)
                            .thenReturn(securityContext))
                    .map(x -> exchange.mutate().request(builder.build()).build())
                    .flatMap(chain::filter);
        };
    }

    @Configuration
    @ConfigurationProperties(prefix = "e2etrust.filter")
    public static class Config {
        private static String e2eTokenHeader;

        public String getE2eTokenHeader() {
            return e2eTokenHeader;
        }

        public void setE2eTokenHeader(String e2eTokenHeader) {
            this.e2eTokenHeader = e2eTokenHeader;
        }
    }
}
