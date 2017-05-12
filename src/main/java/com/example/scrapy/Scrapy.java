package com.example.scrapy;

import com.example.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mqm on 2017/5/11.
 */
@Component
public class Scrapy {
    private final static Logger logger = LoggerFactory.getLogger(Scrapy.class);
    @Autowired
    HttpClientUtil httpClientUtil;
    public void Scrapy(Integer fromPage,Integer toPage,String ku) throws Exception {
        gcft(1,2);
    }

    public void gcft(Integer fromPage,Integer toPage) throws Exception {
        String url = "http://125.35.6.80:8080/ftba/itownet/fwAction.do?method=getBaNewInfoPage";
//        JSONObject paramsJSON = new JSONObject();
//        paramsJSON.put("page", 1);
//        paramsJSON.put("pageSize", 15);
        Map<String,String> params = new HashMap<String,String>();
        params.put("page",1+"");
        params.put("pageSize",15+"");
        String result = httpClientUtil.sendHttpPost(url, params);
        logger.info(result);
    }

}
