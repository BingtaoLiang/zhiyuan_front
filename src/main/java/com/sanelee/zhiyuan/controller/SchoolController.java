package com.sanelee.zhiyuan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SchoolController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.provider.url}")
    private String url;

    @GetMapping("/school")
    public String school(Model model,
                         @RequestParam(name = "page",defaultValue = "1") Integer page,
                         @RequestParam(name = "size",defaultValue = "6") Integer size,
                         @RequestParam(name = "areaid",defaultValue = "-1") Integer areaid,
                         @RequestParam(name = "search",required = false) String search,
                         @RequestParam(name = "type",required = false) String type,
                         @RequestParam(name = "is985",defaultValue = "-1") Integer is985,
                         @RequestParam(name = "is211",defaultValue = "-1") Integer is211,
                         @RequestParam(name = "isdoublefirstclass",defaultValue = "-1") Integer isdoublefirstclass){
        Map map = restTemplate.getForObject(url + "/school?page="+page+"&size="+size+"&areaid="+areaid+"&search="+search+"&type="+type+"&is985="+is985+"&is211="+is211+"&isdoublefirstclass="+isdoublefirstclass, Map.class);


        Object areaList = map.get("areaList");
        Object typeList = map.get("typeList");
        Object pagination = map.get("pagination");
        model.addAttribute("pagination",pagination);
        model.addAttribute("area",areaList);
        model.addAttribute("typeList",typeList);
        model.addAttribute("search",search);
        model.addAttribute("areaid",areaid);
        model.addAttribute("type",type);
        model.addAttribute("is211",is211);
        model.addAttribute("is985",is985);
        model.addAttribute("isdoublefirstclass",isdoublefirstclass);
        return "school";
    }
}
