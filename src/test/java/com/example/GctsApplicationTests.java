package com.example;

import com.alibaba.fastjson.JSONObject;
import com.example.amazonSprider.AmazonUrlSprider;
import com.example.controller.HomeController;
import com.example.entity.AmazonProduct;
import com.example.result.Result;
import com.example.scrapy.JKScrapy;
import com.example.util.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.bytedeco.javacpp.opencv_core;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_GAUSSIAN;
import static org.bytedeco.javacpp.opencv_imgproc.cvSmooth;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GctsApplicationTests {
	private final static Logger logger = LoggerFactory.getLogger(GctsApplicationTests.class);


	@Autowired
	JKScrapy jkScrapy;

	@Test
	public void contextLoads() {
	}
	@Test
	public void testRWU() throws FileNotFoundException {
//		ReadAndWritePoiUtil pu = ReadAndWritePoiUtil.getInstance("C:/HBSData/amazon/r1t.xlsx");
//		AmazonProduct p = new AmazonProduct();
//		p.setAboutThisProduct("123123");
//		p.setCountAnswers("122");
//		p.setId(123);
//		List<String> strings = new ArrayList<>();
//		for(int i= 0 ;i<5;i++){
//			strings.add("aaa"+i);
//
//		}
//		p.setInfoMations(strings);
//		List<AmazonProduct> productList = new ArrayList<>();
//		for(int i  = 0;i<100;i++) {	p.setId(123+i);
//		 productList.add(p);
//		}
//		pu.writeProuctInfo(productList);

	}

	@Test
	public void testUrlSp() throws Exception {
//		AmazonUrlSprider amazonUrlSprider = new AmazonUrlSprider();
//		amazonUrlSprider.startAmazonUrlSprider("https://www.amazon.com/Beauty-Makeup-Skin-Hair-Products/b/ref=nav_shopall_bty?ie=UTF8&node=3760911","Face");
		String res = URLEncoder.encode("Bath Oil/salts/soak","utf-8");
		System.out.println("res:"+res);
	}

	@Test
	public void testjk() throws Exception{
		String urlPath ="http://123.127.80.6/servlet/GetImageServlet?sn=randomImage";
//		org.json.JSONObject params = new org.json.JSONObject();
//		params.put("tableId",69);
//		params.put("State",1);
//		params.put("bcId","124053679279972677481528707165");
//		params.put("tableName","TABLE69");
//		params.put("viewsubTitleName","COLUMN805,COLUMN811");
//		params.put("tableView",URLEncoder.encode("进口化妆品","utf-8"));
		//Result result = UnirestUtil.ajaxPostStr(url,"tableId=69&State=1&tableName=TABLE69&curstart=1");

//		HttpResponse<String> response = Unirest.get("http://123.127.80.6/servlet/GetImageServlet?sn=randomImage")
//				.header("Content-Type","image/jpeg")
//				.asString();
//		int status = response.getStatus();
//		String text = response.getStatusText();
//		if (status == 200 && text.equals("OK")){
//			String is = response.getBody();
//
//			//byte[] bs = jkScrapy.toByteArray(is);
//			//jkScrapy.byte2image(bs,"/Users/mqm/Desktop/a");
//			//return VeriCodeProc.veriCode(toByteArray(is), 60, 20);
//		}else{
//			logger.info("result:"+text);
//		}
//		Connection conn = Jsoup.connect(urlPath).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36");
//		Connection.Response response = conn.execute();
//		System.out.println("urlPath: " + urlPath);
//		System.out.println("conn.getResponseCode():  " + response.statusCode());
//		if (response.statusCode() != 404) {
//			byte[] data = response.bodyAsBytes();
//			FileOutputStream outputStream = new FileOutputStream("/Users/mqm/Desktop/a.png");
//			outputStream.write(data);
//			outputStream.close();
//		}
		String s = jkScrapy.getVeriCode();
		logger.info("ddd:"+s);
	}


}
