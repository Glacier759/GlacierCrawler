package com.glacier.crawler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Glacier on 16/5/10.
 */
@Controller
@RequestMapping(value = "/page")
public class ErrorPageController {

    @ResponseBody
    @RequestMapping(value = "/404")
    public String Page404() {
        return "404 Not Found";
    }

}
