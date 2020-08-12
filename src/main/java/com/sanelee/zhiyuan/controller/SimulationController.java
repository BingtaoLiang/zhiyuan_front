package com.sanelee.zhiyuan.controller;

import com.sanelee.zhiyuan.model.GaoKao;
import com.sanelee.zhiyuan.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class SimulationController {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.provider.url}")
    private String url;

    @RequestMapping("/simulation")
    public String simulation(){
        return "simulation";
    }


    @GetMapping("/simulationResult")
    public String major(HttpServletRequest request,
                        @RequestParam(name="score") Integer score,
                        Model model){
        User user = (User) request.getSession().getAttribute("loginUser");
        String userarea = user.getUserarea();
        String usersort = user.getUsersort();
        Map map = restTemplate.getForObject(url + "/simulationResult?score=" + score+"&usersort="+usersort+"&userarea="+userarea, Map.class);
        Object gaokaoList = map.get("gaokao");
        if ("分数不能为空!".equals(map.get("msg"))){
            model.addAttribute("msg","分数不能为空");
            return "simulation";
        }
        model.addAttribute("gaokao",gaokaoList);
        return "simulationResult";

    }
}
