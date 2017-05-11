package com.example;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mqm on 2017/5/11.
 */
@Component
public class Scrapy {
    private final static Logger logger = LoggerFactory.getLogger(Scrapy.class);
    @Autowired HttpClientUtil httpClientUtil;
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
