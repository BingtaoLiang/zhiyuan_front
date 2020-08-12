package com.sanelee.zhiyuan.controller;

import com.sanelee.zhiyuan.model.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/front/*")
public class AuthorizeController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.provider.url}")
    private String url;

    @Data
    class UserInfo {
        private String phone;
        private String code;
    }

    //注册页面
    @GetMapping("/register")
    public String register() {
        return "register";
    }


    @GetMapping("/")
    public String index(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                String token = cookie.getValue();
                User user = restTemplate.getForObject(url + "/front/?token=" + token, User.class);
                if (user != null) {
                    request.getSession().setAttribute("user", user);
                }
            }
        }
        return "index";
    }


    /*
     * 注册功能
     * */
    @RequestMapping("/addregister")
    public String register(HttpServletRequest request, Map<String, Object> map) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        String userPhone = request.getParameter("userPhone");
        String userArea = request.getParameter("userArea");
        String userSort = request.getParameter("userSort");

        String result = restTemplate.postForObject(url + "/front/addregister?username=" + username + "&password=" + password + "&password2=" + password2 + "&userPhone=" + userPhone + "&userSort=" + userSort + "&userArea=" + userArea, null, String.class);
        if ("login".equals(result)) {  //登录成功
            return "login";
        } else {
            map.put("msg", result);
            return "register";
        }

    }

    //登录页面
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //手机登录页面
    @GetMapping("/sendcode")
    public String sendcode() {
        return "sendcode";
    }

    //验证码页面
    @GetMapping("/phonelogin")
    public String phonelogin() {
        return "phonelogin";
    }

    //登录方法
    @RequestMapping("/addlogin")
    public String addlogin(HttpServletRequest request,
                           HttpServletResponse response,
                           Map<String, Object> map,
                           Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();

        User user = restTemplate.postForObject(url + "/front/addlogin?username=" + username + "&password=" + password, null, User.class);

        if (user != null) {
            String token = UUID.randomUUID().toString();
            user.setToken(token);

            restTemplate.postForObject(url + "/front/updateUser", user, String.class);
            response.addCookie(new Cookie("token", token));
            session.setAttribute("loginUser", user);
            session.setAttribute(user.getUsername(), token);
            map.put("msg", "登陆成功");
            model.addAttribute("user", username);
            System.out.println(session.getId());
            System.out.println(token);
            return "redirect:/";
        } else {
            map.put("msg", "密码或账号错误！");
            return "login";
        }

    }

    //手机号登录
    @RequestMapping("/sendcode")
    public String sendcode(HttpServletRequest request, Map<String, Object> map) {
        String phone = request.getParameter("phonenumber");
        User user = restTemplate.postForObject(url + "/front/sendcode?phonenumber=" + phone, null,User.class);

        if (user != null) {
            HttpSession session = request.getSession();
            Map resultMap = restTemplate.getForObject(url + "/front/getMessage?phone="+phone, Map.class);
            if ("-1".equals(resultMap.get("sms"))) {
                map.put("msg", "获取验证码失败,请稍后重试或联系管理员！");
                return "sendcode";
            }
            UserInfo userInfo =new UserInfo();
            userInfo.setPhone(phone);
            String code = (String) resultMap.get("code");
            userInfo.setCode(code);
            session.setAttribute("userInfo", userInfo);
            return "phonelogin";
        } else {
            map.put("msg", "手机号未注册，请先注册！");
            return "sendcode";
        }

    }

    //输入验证码
    @RequestMapping("/phonelogin")
    public String phonelogin(HttpServletRequest request,
                             Map<String, Object> map){
        HttpSession session = request.getSession();
        String code = request.getParameter("code");
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
        if (! userInfo.getCode().equals(code)) {
            map.put("msg", "验证码输入错误！");
            return "phonelogin";
        } else {
            String phone = userInfo.getPhone();
            User user = restTemplate.postForObject(url + "/front/phonelogin?phone=" + phone,null,User.class);
            session.setAttribute("loginUser", user);
            map.put("msg", "登陆成功");
            return "redirect:/";
        }

    }

    //退出登录
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("loginUser");
        return "redirect:/";
    }

}
