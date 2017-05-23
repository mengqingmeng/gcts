package com.example.scrapy;

import com.example.entity.JKProduct;
import com.example.result.Result;
import com.example.util.UnirestUtil;
import com.example.util.VeriCodeProc;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sun.jvm.hotspot.runtime.Bytes;

import javax.imageio.stream.FileImageOutputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mqm on 2017/5/23.
 */
@Component
public class JKScrapy {
    private final static Logger logger = LoggerFactory.getLogger(JKScrapy.class);

    public void jk(Integer pageIndex) throws Exception {
        String url = "http://app1.sfda.gov.cn/datasearch/face3/search.jsp";
        Result result = UnirestUtil.ajaxPostStr(url,"tableId=69&State=1&tableName=TABLE69&curstart="+pageIndex);
        if (result.getCode()==200 && result.getMessage().equals("OK")){
            String data = (String) result.getData();
            Document doc = Jsoup.parse(data);
            Elements as = doc.getElementsByTag("a");
            List<JKProduct> products = new ArrayList<JKProduct>();
        }
    }



    public String getVeriCode() throws UnirestException, IOException {
        String urlPath = "http://123.127.80.6/servlet/GetImageServlet?sn=randomImage";

        Connection conn = Jsoup.connect(urlPath).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36");
        Connection.Response response = conn.execute();
        System.out.println("urlPath: " + urlPath);
        System.out.println("conn.getResponseCode():  " + response.statusCode());
        if (response.statusCode() != 404) {
            byte[] data = response.bodyAsBytes();
            //String result = VeriCodeProc.veriCode(data,60,20);
            //logger.info("result:"+result);
            FileOutputStream outputStream = new FileOutputStream("/Users/mqm/Desktop/a.png");
            outputStream.write(data);
            outputStream.close();
        }

        return "";
    }

}
