package com.example.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GetDoc {

	public static Document getDoc (String url){
		try {
			Document  doc = Jsoup.connect(url)
				    .header("Accept-Encoding", "gzip, deflate")  
				    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")  
				    .maxBodySize(0)  
				    .timeout(600000000)  
				    .get();
			return doc;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
