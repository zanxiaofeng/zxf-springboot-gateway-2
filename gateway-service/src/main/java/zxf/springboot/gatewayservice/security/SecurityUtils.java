package zxf.springboot.gatewayservice.security;

import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;

public class SecurityUtils {

    public static String getTokenIdFromRequest(ServerHttpRequest request) {
        HttpCookie tokenCookie = request.getCookies().getFirst("Token");
        if (tokenCookie != null) {
            return tokenCookie.getValue();
        }

        return request.getHeaders().getFirst("X-Token");
    }
}
