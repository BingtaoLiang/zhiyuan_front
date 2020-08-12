package com.sanelee.zhiyuan.controller;

import com.sanelee.zhiyuan.model.Profession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Controller
public class ProfessionController {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.provider.url}")
    private String url;

    @RequestMapping("/profession")
    public String Profession(Model model,
                             @RequestParam(name = "page",defaultValue = "1") Integer page,
                             @RequestParam(name = "size",defaultValue = "10") Integer size,
                             @RequestParam(name = "major",required = false) String major,
                             @RequestParam(name = "search",required = false) String search,
                             @RequestParam(name = "subject",required = false) String subject){
        Map map = restTemplate.getForObject(url + "/profession?page=" + page + "&size=" + size + "&major=" + major + "&search=" + search + "&subject=" + subject, Map.class);

        //根据major获取subject
        List<Profession> subjectListByMajor = restTemplate.getForObject(url + "/getSubjectByMajor?major=" + major, List.class);
        Object majorinfo = map.get("majorinfo");
        Object pagination = map.get("pagination");
        List<Profession> majorList = (List<Profession>) map.get("majorList");
//        List<Profession> subjectListByMajor = (List<Profession>) map.get("subjectListByMajor");

        model.addAttribute("majorinfo",majorinfo);
        model.addAttribute("pagination",pagination);
        model.addAttribute("majorList",majorList);
        model.addAttribute("subjectList",subjectListByMajor);
        model.addAttribute("subject",subject);
        model.addAttribute("major",major);
        model.addAttribute("search",search);

        return "profession";

    }
}
