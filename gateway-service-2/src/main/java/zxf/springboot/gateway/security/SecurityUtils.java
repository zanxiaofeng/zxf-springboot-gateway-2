package zxf.springboot.gateway.security;

import io.lettuce.core.output.BooleanOutput;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.util.WebUtils;
import zxf.springboot.authentication.MyAuthentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;

public class SecurityUtils {

    public static String getTokenIdFromRequest(HttpServletRequest request) {
        Cookie tokenCookie = WebUtils.getCookie(request, "Token");
        if (tokenCookie != null) {
            return tokenCookie.getValue();
        }

        return request.getHeader("X-Token");
    }

    public static String getCurrentAccessToken() {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof MyAuthentication)) {
            return null;
        }
        return ((MyAuthentication) SecurityContextHolder.getContext().getAuthentication()).getAccessToken();
    }

    public static String getCurrentRefreshToken() {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof MyAuthentication)) {
            return null;
        }
        return ((MyAuthentication) SecurityContextHolder.getContext().getAuthentication()).getRefreshToken();
    }

    public static Boolean isCurrentAccessTokenExpired() {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof MyAuthentication)) {
            return false;
        }
        return ((MyAuthentication) SecurityContextHolder.getContext().getAuthentication()).getAccessTokenExpiryTime().isAfter(ZonedDateTime.now());
    }

    public static void setCurrentAccessToken(String accessToken) {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof MyAuthentication)) {
            return;
        }
        ((MyAuthentication) SecurityContextHolder.getContext().getAuthentication()).setAccessToken(accessToken);
        ((MyAuthentication) SecurityContextHolder.getContext().getAuthentication()).setNeedSave(true);
    }

    public static MyAuthentication.MyUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (MyAuthentication.MyUser) authentication.getPrincipal();
    }
}
