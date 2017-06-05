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
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import org.json.Cookie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;

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
	public void tess4j() throws IOException {
		File imageFile = new File("/Users/mqm/Desktop/a.png");
		BufferedImage grayImage = ImageHelper.convertImageToBinary(ImageIO.read(imageFile));
		ITesseract instance = new Tesseract();  // JNA Interface Mapping
		// ITesseract instance = new Tesseract1(); // JNA Direct Mapping
		try {
			String result = instance.doOCR(grayImage);
			System.out.println(result);
		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}
	}

	@Test
	public void TT() throws UnirestException, IOException {

		Map<String, String> cookies = null;
		Connection conn = null;
		Connection.Response response=null;
		if (cookies==null){
			conn = Jsoup.connect("http://123.127.80.6/sfda/ShowJSYQAction.do");
			response = conn.execute();
			cookies =response.cookies();
		}
		String code = jkScrapy.getVeriCode(cookies);
		conn = Jsoup.connect("http://123.127.80.6/sfda/ShowJSYQAction.do?"+"PID=4a7f362c3aff91c0d83b05d526e71e9e&randomInt="+code+"&process=showNew")
				.timeout(10000).cookies(cookies);
		response = conn.execute();
		String body = response.body();
		logger.info("body:"+body);
	}

	@Test
	public void dd() throws IOException {
		Map<String, String> cookies = null;
		String url ="http://123.127.80.6/sfda/ShowJSYQAction.do?PID=4a7f362c3aff91c0d83b05d526e71e9e";
		if (cookies==null){
			Connection conn = Jsoup.connect(url);
			Connection.Response response = conn.execute();
			cookies=response.cookies();
		}
		Connection conn = Jsoup.connect(url).cookies(cookies);
		Connection.Response response = conn.execute();
		String body = response.body();
		logger.info("body:"+body);
	}

	@Test
	public void xx(){
		for (int i =0;i<10;i++) {
			Random random = new Random();
			int index = random.nextInt(10)+1;
			logger.info(index+"");
		}

	}
}
