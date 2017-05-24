package com.example.util;

public class VeriCodeProc {
	public static native String veriCode(byte[] bs, int w, int h);
	
	static{
		//String property = System.getProperty("java.library.path");
		//System.out.println("red.dll path:"+property);
		System.load("D:\\Workspaces\\IDEA\\hbsScrapy\\src\\main\\java\\com\\example\\util\\rec.dll");
		//System.loadLibrary("rec");
	}
}
