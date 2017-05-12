package com.example;

import com.example.entity.AmazonProduct;
import com.example.util.ReadAndWritePoiUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GctsApplicationTests {

	@Test
	public void contextLoads() {
	}
	@Test
	public void testRWU(){
		ReadAndWritePoiUtil pu = new ReadAndWritePoiUtil("d:/test.xls");
		AmazonProduct p = new AmazonProduct();
		pu.writeTitleName(p);
	}
}
