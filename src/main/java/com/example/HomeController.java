package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mqm on 2017/5/10.
 */
@Controller
public class HomeController {

    @GetMapping(value = "/hi")
    public String home(){
        return "index";
    }
}
