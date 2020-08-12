package com.sanelee.zhiyuan.controller;

import com.sanelee.zhiyuan.model.Profession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class HighSearchController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.provider.url}")
    private String url;

    @RequestMapping("/highSearch")
    public String highSearch(Model model) {
        List<Profession> professionList = restTemplate.getForObject(url + "/highSearch", List.class);
        model.addAttribute("professionList", professionList);
        return "highSearch";
    }

    @RequestMapping(value = "/search",method= RequestMethod.POST)
    public String search(Model model,
                         HttpServletRequest request,
                         @RequestParam(name = "area",required = false) String area,
                         @RequestParam(name = "profession",required = false) String profession,
                         @RequestParam(name = "type",required = false) Integer type
    ){
        Map map = restTemplate.postForObject(url + "/search?area=" + area + "&profession=" + profession + "&type=" + type,null,Map.class);
        if ("专业为必选项".equals(map.get("msg"))){
            Object professionList = map.get("professionList");
            model.addAttribute("professionList",professionList);
            return "highSearch";
        }else{
            Object professionList = map.get("professionList");
            Object schoolSearchList = map.get("schoolSearchList");
            model.addAttribute("professionList", professionList);
            model.addAttribute("schoolList", schoolSearchList);
            model.addAttribute("area", area);
            model.addAttribute("profession", profession);
            model.addAttribute("type", type);
            return "highSearch";

        }
    }


}
