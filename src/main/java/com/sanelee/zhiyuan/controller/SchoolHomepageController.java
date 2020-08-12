package com.sanelee.zhiyuan.controller;

import com.sanelee.zhiyuan.model.GaoKao;
import com.sanelee.zhiyuan.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class SchoolHomepageController {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.provider.url}")
    private String url;

    @RequestMapping("/schoolHomepage/{scid}")
    public String schoolHomepage(@PathVariable(name = "scid") Integer scid,
                                 @RequestParam(name = "province", defaultValue = "陕西") String province,
                                 @RequestParam(name = "sort", defaultValue = "1") Integer sort,
                                 Model model){
        Map map = restTemplate.getForObject(url + "/schoolHomepage/" + scid+"?sort="+sort+"&province="+province, Map.class);
        Object schools = map.get("schools");
        Object schoolscores = map.get("schoolscores");
        Object areaList = map.get("areaList");
//        Object gaoKao = map.get("gaoKao");

        List<GaoKao> gaoKao = restTemplate.getForObject(url + "/schoolHomepage2/" + scid+"?sort="+sort+"&province="+province, List.class);
        model.addAttribute("schools",schools);
        model.addAttribute("schoolscores",schoolscores);
        model.addAttribute("sort",sort);
        model.addAttribute("area",areaList);
        model.addAttribute("scores",gaoKao);

        return "schoolHomepage";
    }
}
