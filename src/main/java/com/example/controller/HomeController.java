package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.beautyPediaSprider.PediaService;
import com.example.entity.BeautyCategory;
import com.example.scrapy.GcftScrapy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
    
    @RequestMapping("/BeautyPedia")
    public  String BeautyPedia(ModelMap model){
    	List<BeautyCategory> categoryList = PediaService.updateCategory();
    	List<BeautyCategory> categoryListS = new ArrayList<BeautyCategory>();
    	List<BeautyCategory> categoryListM = new ArrayList<BeautyCategory>();
    	List<BeautyCategory> categoryListB = new ArrayList<BeautyCategory>();
    	for(BeautyCategory  b : categoryList){
    		switch (b.getType().ordinal()) {
			case 0:
				categoryListS.add(b);
				break;
			case 1:
				categoryListM.add(b);
				break;
			case 2:
				categoryListB.add(b);
				break;
			}
    	}
    	model.put("categoryListB", categoryListB);
    	model.put("categoryListM", categoryListM);
    	model.put("categoryListS", categoryListS);
        return "BeautyPedia";
    }
    @ResponseBody
    @RequestMapping("/getinfo")
    public String Getinfo(@RequestParam("proId") Integer proId){
		System.out.println(proId);
		PediaService.main(proId);
    	return "结束";
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
