package com.example.util;

import java.io.File;
import java.net.URI;

public class VeriCodeProc {
	public static native String veriCode(byte[] bs, int w, int h);
	
	static{
		//String property = System.getProperty("java.library.path");
		System.load("/Users/mqm/Desktop/web/workspace/idea/hbsScrapy/src/main/java/com/example/util/rec.dll");
	}
}
