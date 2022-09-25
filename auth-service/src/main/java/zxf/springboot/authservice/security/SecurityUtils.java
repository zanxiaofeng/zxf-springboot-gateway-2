package zxf.springboot.authservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.util.WebUtils;
import zxf.springboot.authentication.MyAuthentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityUtils {
    public static void logon(HttpServletResponse response, String username) {
        MyAuthentication myAuthentication = new MyAuthentication(new MyAuthentication.MyUser(username));
        SecurityContextHolder.getContext().setAuthentication(myAuthentication);
        saveTokenIdToResponse(response, myAuthentication.getTokenId());
    }

    public static String getTokenIdFromRequest(HttpServletRequest request) {
        Cookie tokenCookie = WebUtils.getCookie(request, "Token");
        if (tokenCookie != null) {
            return tokenCookie.getValue();
        }

        return request.getHeader("X-Token");
    }

    public static void saveTokenIdToResponse(HttpServletResponse response, String tokenId) {
        Cookie tokenCookie = new Cookie("Token", tokenId);
        tokenCookie.setMaxAge(3600 * 3);
        tokenCookie.setPath("/");
        tokenCookie.setSecure(true);
        tokenCookie.setHttpOnly(true);
        response.addCookie(tokenCookie);

        response.addHeader("X-Token", tokenId);
    }

    public static String getCurrentAccessToken() {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof MyAuthentication)) {
            return null;
        }
        return ((MyAuthentication) SecurityContextHolder.getContext().getAuthentication()).getAccessToken();
    }

    public static MyAuthentication.MyUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (MyAuthentication.MyUser) authentication.getPrincipal();
    }
}
