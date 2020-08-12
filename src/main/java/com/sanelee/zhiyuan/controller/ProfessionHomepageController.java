package com.sanelee.zhiyuan.controller;

import com.sanelee.zhiyuan.dto.PaginationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class ProfessionHomepageController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.provider.url}")
    private String url;

    @RequestMapping("/professionHomepage/{proname}")
    public String professionHomepage(Model model,
                                     @RequestParam(name = "page",defaultValue = "1") Integer page,
                                     @RequestParam(name = "size",defaultValue = "5") Integer size,
                                     @PathVariable(name = "proname") String proname){
        Map map = restTemplate.getForObject(url + "/professionHomepage/" + proname+"?page="+page, Map.class);

        Object profession = map.get("profession");
        Object professionSchool =  map.get("professionSchool");
        model.addAttribute("profession",profession);
        model.addAttribute("professionSchool",professionSchool);
        model.addAttribute("proname",proname);
        model.addAttribute("size",size);
        return "professionHomepage";

    }
}
