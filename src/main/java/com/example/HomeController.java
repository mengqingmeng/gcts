package com.example;

import com.example.scrapy.GcftScrapy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mqm on 2017/5/10.
 */
@Controller

public class HomeController {
    private final static Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    GcftScrapy gcftScrapy;

    @RequestMapping("/")
    public String home(){
        return "index";
    }

    @ResponseBody
    @RequestMapping("/start")
    public String start(@RequestParam("fromPage") Integer fromePage,
                        @RequestParam("endPage") Integer endPage,
                        @RequestParam("ku") String ku) throws Exception {
        gcftScrapy.Scrapy(fromePage,endPage,ku);
        return "succ";
    }
}
