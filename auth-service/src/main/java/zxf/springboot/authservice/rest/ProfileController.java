package zxf.springboot.authservice.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import zxf.springboot.authentication.MyAuthentication;
import zxf.springboot.authservice.security.SecurityUtils;


import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private static final String MODEL_AND_VIEW_OBJECT_KEY_SITE_URL = "siteUrl";
    private static final String MODEL_AND_VIEW_OBJECT_KEY_SESSION_ID = "SessionId";
    private static final String MODEL_AND_VIEW_OBJECT_KEY_PRINCIPAL = "principal";

    @Value("${site.url}")
    private String siteUrl;

    @GetMapping("/home")
    public ModelAndView home(HttpSession session) {
        logInfo("home", session);

        ModelAndView modelAndView = new ModelAndView("home-page");
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_SITE_URL, siteUrl);
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_SESSION_ID, session.getId());
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_PRINCIPAL, SecurityUtils.getCurrentUser());
        return modelAndView;
    }

    @GetMapping("/profile")
    public ModelAndView profile(HttpSession session) {
        logInfo("profile", session);

        ModelAndView modelAndView = new ModelAndView("profile-page");
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_SITE_URL, siteUrl);
        modelAndView.addObject(MODEL_AND_VIEW_OBJECT_KEY_SESSION_ID, session.getId());
        return modelAndView;
    }

    @PostMapping("/profile-form")
    public ModelAndView profileForm(HttpSession session, @RequestParam Integer age) {
        logInfo("profile-form", session);

        //Will auto save
        MyAuthentication.MyUser myUser = SecurityUtils.getCurrentUser();
        myUser.setAge(age);

        return new ModelAndView("redirect:" + siteUrl + "/profile/home");
    }

    private void logInfo(String method, HttpSession session) {
        String accessToken = SecurityUtils.getCurrentAccessToken();
        System.out.println("ProfileController::" + method + ", " + session.getId() + ", accessToken = " + accessToken);
        SecurityUtils.setCurrentAccessToken(accessToken + "-" + method);
    }
}
