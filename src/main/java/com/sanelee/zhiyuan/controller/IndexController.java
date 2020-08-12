package com.sanelee.zhiyuan.controller;


import com.sanelee.zhiyuan.dto.PaginationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class IndexController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.provider.url}")
    private String url;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "30") Integer size) {

        PaginationDTO videos = restTemplate.getForObject(url + "/?page=" + page + "&size=" + size, PaginationDTO.class);
        model.addAttribute("videos", videos);
        return "index";
    }




}
