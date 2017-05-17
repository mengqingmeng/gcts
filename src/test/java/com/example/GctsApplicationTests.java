package com.example;

import com.example.amazonSprider.AmazonUrlSprider;
import com.example.entity.AmazonProduct;
import com.example.util.DateUtil;
import com.example.util.ReadAndWritePoiUtil;
import com.example.util.UnirestUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GctsApplicationTests {

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

}
