package zxf.springboot.authservice.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import zxf.springboot.authservice.security.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final String HTTP_HEADER_NAME_X_E2E_Trust_Token = "X-E2E-Trust-Token";
    private static final String MODEL_AND_VIEW_OBJECT_KEY_SITE_URL = "siteUrl";
    private static final String MODEL_AND_VIEW_OBJECT_KEY_SESSION_ID = "SessionId";
    private static final String MODEL_AND_VIEW_OBJECT_KEY_PRINCIPAL = "principal";

    @Value("${site.url}")
    private String siteUrl;

    @GetMapping("/logon")
    public ModelAndView logon(HttpSession session) {
        logInfo("logon", session);

        ModelAndView modelAndView = new ModelAndView("logon-page");
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_SITE_URL, siteUrl);
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_SESSION_ID, session.getId());
        return modelAndView;
    }

    @PostMapping("/logon-form")
    public ModelAndView logonForm(HttpServletResponse response, HttpSession session, @RequestParam String name, @RequestParam String passwd) {
        logInfo("logon-form", session);

        if (!"davis".equals(name) || !"davis".equals(passwd)) {
            return new ModelAndView("redirect:" + siteUrl + "/auth/logon-failed");
        }

        SecurityUtils.logon(response, name);

        return new ModelAndView("redirect:" + siteUrl + "/auth/logon-succeed");
    }

    @GetMapping("/logon-failed")
    public ModelAndView logonFailed(HttpSession session) {
        logInfo("logon-failed", session);

        ModelAndView modelAndView = new ModelAndView("logon-failed-page");
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_SITE_URL, siteUrl);
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_SESSION_ID, session.getId());
        return modelAndView;
    }

    @GetMapping("/logon-succeed")
    public ModelAndView logonSucceed(HttpSession session, HttpServletRequest request) {
        logInfo("logon-succeed", session);
        System.out.println("AuthController::, e2eToken = " + request.getHeader(HTTP_HEADER_NAME_X_E2E_Trust_Token));

        ModelAndView modelAndView = new ModelAndView("logon-succeed-page");
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_SITE_URL, siteUrl);
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_SESSION_ID, session.getId());
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_PRINCIPAL, SecurityUtils.getCurrentUser());
        return modelAndView;
    }

    private void logInfo(String method, HttpSession session) {
        String accessToken = SecurityUtils.getCurrentAccessToken();
        System.out.println("AuthController::" + method + ", " + session.getId() + ", accessToken = " + accessToken);
        SecurityUtils.setCurrentAccessToken(accessToken + "-" + method);
    }
}
