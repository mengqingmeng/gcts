package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.amazonSprider.AmazonUrlSprider;
import com.example.beautyPediaSprider.PediaService;
import com.example.entity.BeautyCategory;
import com.example.entity.EWGProduct;
import com.example.scrapy.GcftScrapy;

import com.example.util.UnirestUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by mqm on 2017/5/10.
 */
@Controller
public class HomeController {
    private final static Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    GcftScrapy gcftScrapy;
    @Autowired
    AmazonUrlSprider aus;

    @RequestMapping("/gcft")
    public String home() {
        return "gcft";
    }

    @RequestMapping("/BeautyPedia")
    public String BeautyPedia(ModelMap model) {
        List<BeautyCategory> categoryList = PediaService.updateCategory();
        List<BeautyCategory> categoryListS = new ArrayList<BeautyCategory>();
        List<BeautyCategory> categoryListM = new ArrayList<BeautyCategory>();
        List<BeautyCategory> categoryListB = new ArrayList<BeautyCategory>();
        for (BeautyCategory b : categoryList) {
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

    /**
     * 药监局爬虫
     * @param fromePage 开始页码
     * @param endPage   结束页码
     * @param ku    库的类型 进口库还是国产非特殊用途
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/startGcft")
    public String startGcft(@RequestParam("fromPage") Integer fromePage,
                            @RequestParam("endPage") Integer endPage,
                            @RequestParam("ku") String ku) throws Exception {
        gcftScrapy.Scrapy(fromePage, endPage, ku);
        return "succ";
    }

    /**
     * ewg 爬虫列表展示页
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/ewg")
    public String ewg(ModelMap model) throws Exception {
        String url = "https://www.ewg.org/skindeep/";
        Document doc = Jsoup.connect(url).get();
        Elements newsHeadlines = doc.select(".menuhover");
        JSONArray jobs = new JSONArray();//要执行的爬虫任务列表
        if (newsHeadlines.size()>0){
            for (Element element:newsHeadlines) {
                Elements children = element.children();
                if (children.size() > 0) {
                    Element submenu = children.get(0);//分类名称
                    Element sub = children.get(1);//分类内容

                    JSONObject job = new JSONObject();
                    job.put("name", submenu.text());

                    //分类内容中，又有细分类，所以这里建个数组存放，细分类包含名称和链接+名称
                    JSONArray classifies = new JSONArray();
                    for (Element ul : sub.getElementsByTag("ul")) {
                        JSONObject typeWithUrls = new JSONObject();//细分类
                        String type = ul.child(0).getElementsByTag("h2").text();//细分类名称
                        typeWithUrls.put("type", type);
                        JSONArray urlWithNames = new JSONArray();//细分类链接+名称
                        Elements as = ul.getElementsByTag("a");
                        for (Element a : as) {//添加链接和名称
                            JSONObject urlWithName = new JSONObject();
                            urlWithName.put("url", a.attr("href"));
                            urlWithName.put("name", a.text());
                            urlWithNames.add(urlWithName);
                        }
                        typeWithUrls.put("urls", urlWithNames);

                        classifies.add(typeWithUrls);
                    }

                    job.put("classifies", classifies);
                    jobs.add(job);
                }
            }
        }
        //logger.info("jobs:"+jobs.toString());
        model.put("jobs",jobs);
        return "ewg";
    }

    @ResponseBody
    @RequestMapping("/ewgProduct")
    public String ewgProduct(@RequestParam("url") String  url) throws IOException {

        return "爬取成功";

    }

    @RequestMapping("/amazon")
    public  String amazon(ModelMap model){
        try {
            Map<String,String> urls = null;
            List<Map<String,String>> urlss = new ArrayList<>();
            Map<String,Map<String,String>> allUrls = aus.getAllUrls("https://www.amazon.com/Beauty-Makeup-Skin-Hair-Products/b/ref=topnav_storetab_beauty_sn_fo?ie=UTF8&node=3760911");
            model.put("allUrls",allUrls);
			/*Set<Map.Entry<String ,Map<String,String>>> entry = allUrls.entrySet();
			for(Map.Entry e : entry){
				Set<Map.Entry<String,String>> entry1 = ((Map<String,String>)e.getValue()).entrySet();

				for(Map.Entry e1 : entry1){
					urls =aus.getUrls((String)e1.getValue());
					urlss.add(urls);
					System.out.println(e1.getKey());
					System.out.println(e1.getValue());

				}
			}
			model.put("urls",urlss);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Amazon";
    }



    @RequestMapping("/amazon/Wait")
    public  String amazonWait(String url ,ModelMap model){
        model.put("url",url);
        return "AmazonWait";
    }


    @ResponseBody
    @RequestMapping("/amazon/Sp")
    public  String amazonStart(String url){
        String[] urls = url.split("%:%");
        for(String s : urls) {
            aus.startAmazonUrlSprider(s);
        }
        return "获取完成";
    }
}

