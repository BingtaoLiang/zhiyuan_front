package com.sanelee.zhiyuan.controller;

import com.sanelee.zhiyuan.model.GaoKaoQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class GaoKaoQuestionController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.provider.url}")
    private String url;


    @GetMapping("/gaokaoQuestion")
    public String gaokaoQuestion(Model model) {

        Map map = restTemplate.getForObject(url+"/gaokaoQuestion", Map.class);
        Object questionType = map.get("questionType");
        Object allQuestions = map.get("allQuestions");
        model.addAttribute("questionType", questionType);
        model.addAttribute("allQuestions", allQuestions);
        return "gaokaoQuestion";
    }

    @GetMapping("/questionDetail/{id}")
    public String questionDetail(@PathVariable("id") Integer id, Model model) {
        GaoKaoQuestion questionById = restTemplate.getForObject(url + "/questionDetail/" + id, GaoKaoQuestion.class);
        model.addAttribute("questionById", questionById);
        return "questionDetail";
    }

}
