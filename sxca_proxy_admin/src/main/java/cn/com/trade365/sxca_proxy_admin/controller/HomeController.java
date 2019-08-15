package cn.com.trade365.sxca_proxy_admin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author :lhl
 * @create :2018-11-07 17:21
 */
@Controller
public class HomeController {

    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;

    @RequestMapping({"","repeat","home","index"})
    public String login() {
        return "pages/login";
    }


    @RequestMapping({"/login"})
    public String login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (this.username.equalsIgnoreCase(username) && this.password.equalsIgnoreCase(password)) {
            return "pages/message/list";
        }
        return "pages/login";
    }
}
