package com.example;

import com.example.amazonSprider.AmazonUrlSprider;
import com.example.entity.AmazonProduct;
import com.example.util.ReadAndWritePoiUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
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
		ReadAndWritePoiUtil pu = ReadAndWritePoiUtil.getInstance("D:/Data/amazon/r1t.xlsx");
		AmazonProduct p = new AmazonProduct();
		p.setAboutThisProduct("123123");
		p.setCountAnswers("122");
		p.setId(123);
		p.setAmazonProductInfoMations("w<:>s<:>SDa<:>fas");
		List<AmazonProduct> productList = new ArrayList<>();
		for(int i  = 0;i<100000;i++) {	p.setId(123+i);
		 productList.add(p);
		}
		pu.writeProuctInfo(productList);

	}

	@Test
	public void testUrlSp(){
		AmazonUrlSprider amazonUrlSprider = new AmazonUrlSprider();
		amazonUrlSprider.startAmazonUrlSprider("https://www.amazon.com/Beauty-Makeup-Skin-Hair-Products/b/ref=nav_shopall_bty?ie=UTF8&node=3760911","Face");
	}

}
