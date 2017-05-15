package com.example.beautyPediaSprider;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.entity.BeautyProduct;
import com.example.util.ReadAndWritePoiUtil;
import org.springframework.boot.autoconfigure.jersey.JerseyProperties.Type;

import com.example.entity.BeautyCategory;

/**
 * 通过URL读取页面内容
 * */

public class PediaService {

	public static List<BeautyCategory> categoryList = new ArrayList<>();

    public static void main(int proId){
		String url = ""; //通过类别id获得url

		for (BeautyCategory  b : categoryList ){
			if (b.getCategoryId()==proId){
				url=b.getUrl();
			}
		}
    	 System.out.println("通过类别id获得的url："+url);
    	 int page  = GetPediaInfo.GetPage(url);             //通过类别url获得页数
    	 System.out.println("通过类别url获得页数 :" +page);
    	 List<BeautyProduct> BeautyProducts = new ArrayList<>();
    	 for(int i =1; i<=page;i++){
    		 Date startDate = new Date();
    		 SimpleDateFormat sdf  = new SimpleDateFormat("hh:mm:ss");
    		 System.out.println("开始执行第"+i+"页,当前时间 ："+sdf.format(startDate));
    		 String pageUrl = GetPediaInfo.getPageUrl(url, i);           //获得翻页后的类别url
    		 System.out.println("得翻页后的类别url:"+pageUrl);
    		 List<String> urls = GetPediaInfo.getUrlList(pageUrl);   //通过类别url获得单页url
    		 for(String s : urls){
				 BeautyProducts.add(GetPediaInfo.GetProductDetail(s));//过每个URL爬取产品信息
    		 }
    		 Date endDate = new Date();
    		 long longTime = (endDate.getTime()-startDate.getTime())/1000;
    		 long mm =  longTime/60;
    		 long ss = longTime%60;
			 try {
			 	 String path = "d:/text.xlsx";
				 ReadAndWritePoiUtil eadAndWritePoiUtil= ReadAndWritePoiUtil.getInstance(path);  //保存路径
				 eadAndWritePoiUtil.writeProuctInfo(BeautyProducts);                                  //保存
			 } catch (FileNotFoundException e) {
				 e.printStackTrace();
			 }


			 System.out.println("第"+i+"页结束，耗时"+mm+"分"+ss+"秒");

    	 } 
    	 System.out.println("当前类别执行完毕!");
    }
    /**
     * 获得类别列表
     * @return
     */
    
    public static List<BeautyCategory> updateCategory (){
    	return categoryList = GetPediaInfo.GetCategory();
    }
    /**
     * 获得枚举类型
     * @return
     */
    public static List<String> GetType(){
    	List<String> list = new ArrayList();
    	for(Type t :Type.values()){
    		list.add(t.toString());
    	}
    	return list;
    }   
}
