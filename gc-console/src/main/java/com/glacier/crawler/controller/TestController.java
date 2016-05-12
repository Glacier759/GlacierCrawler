package com.glacier.crawler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glacier on 16/5/11.
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {

    @RequestMapping(value = "/hello")
    public String test(Model model) {
        List<String> list = new ArrayList<>();
        list.add("hhhhh");
        list.add("bbbbb");
        model.addAttribute("hello", list);
        return "index";
    }

}
