package zxf.springboot.gatewayservice.security;

import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContext;
import zxf.springboot.authentication.MyAuthentication;

import java.time.ZonedDateTime;

public class SecurityUtils {

    public static String getTokenIdFromRequest(ServerHttpRequest request) {
        HttpCookie tokenCookie = request.getCookies().getFirst("Token");
        if (tokenCookie != null) {
            return tokenCookie.getValue();
        }

        return request.getHeaders().getFirst("X-Token");
    }

    public static Boolean isCurrentAccessTokenExpired(SecurityContext securityContext) {
        if (!(securityContext.getAuthentication() instanceof MyAuthentication)) {
            return false;
        }
        ZonedDateTime accessTokenExpiryTime = ZonedDateTime.parse(((MyAuthentication) securityContext.getAuthentication()).getAccessTokenExpiryTime());
        return accessTokenExpiryTime.isBefore(ZonedDateTime.now());
    }

    public static String getCurrentAccessToken(SecurityContext securityContext) {
        if (!(securityContext.getAuthentication() instanceof MyAuthentication)) {
            return null;
        }
        return ((MyAuthentication) securityContext.getAuthentication()).getAccessToken();
    }

    public static String getCurrentRefreshToken(SecurityContext securityContext) {
        if (!(securityContext.getAuthentication() instanceof MyAuthentication)) {
            return null;
        }
        return ((MyAuthentication) securityContext.getAuthentication()).getRefreshToken();
    }

    public static void setCurrentAccessToken(SecurityContext securityContext, String accessToken) {
        if (!(securityContext.getAuthentication() instanceof MyAuthentication)) {
            return;
        }
        ((MyAuthentication) securityContext.getAuthentication()).setAccessToken(accessToken);
        ((MyAuthentication) securityContext.getAuthentication()).setNeedSave(true);
    }
}
