package zxf.springboot.authservice.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import zxf.springboot.authentication.MyAuthentication;
import zxf.springboot.authservice.security.SecurityUtils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private static final String HTTP_HEADER_NAME_X_E2E_Trust_Token = "X-E2E-Trust-Token";
    private static final String MODEL_AND_VIEW_OBJECT_KEY_SITE_URL = "siteUrl";
    private static final String MODEL_AND_VIEW_OBJECT_KEY_SESSION_ID = "SessionId";
    private static final String MODEL_AND_VIEW_OBJECT_KEY_PRINCIPAL = "principal";

    @Value("${site.url}")
    private String siteUrl;

    @GetMapping("/home")
    public ModelAndView home(HttpServletRequest request, HttpSession session) {
        logInfo("home", request, session);

        ModelAndView modelAndView = new ModelAndView("home-page");
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_SITE_URL, siteUrl);
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_SESSION_ID, session.getId());
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_PRINCIPAL, SecurityUtils.getMyAuthentication().getMyUser());
        return modelAndView;
    }

    @GetMapping("/profile")
    public ModelAndView profile(HttpServletRequest request, HttpSession session) {
        logInfo("profile", request, session);

        ModelAndView modelAndView = new ModelAndView("profile-page");
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_SITE_URL, siteUrl);
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_SESSION_ID, session.getId());
        return modelAndView;
    }

    @PostMapping("/profile-form")
    public ModelAndView profileForm(HttpServletRequest request, HttpSession session, @RequestParam Integer age) {
        logInfo("profile-form", request, session);

        //Will auto save
        MyAuthentication myAuthentication = SecurityUtils.getMyAuthentication();
        myAuthentication.getMyUser().setAge(age);
        myAuthentication.setNeedSave(true);

        return new ModelAndView("redirect:" + siteUrl + "/profile/home");
    }

    private void logInfo(String method, HttpServletRequest request, HttpSession session) {
        String accessToken = SecurityUtils.getCurrentAccessToken();
        System.out.println("ProfileController::" + method + ", " + session.getId() + ", accessToken = " + accessToken
                + ", e2eToken = " + request.getHeader(HTTP_HEADER_NAME_X_E2E_Trust_Token));
    }
}
