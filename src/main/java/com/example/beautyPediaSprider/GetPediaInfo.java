package com.example.beautyPediaSprider;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.entity.BeautyCategory;
import com.example.entity.BeautyCategory.Type;
import com.example.entity.BeautyProduct;
import com.example.util.GetDoc;


public class GetPediaInfo {

    /**
     * 通过类别url获得单页URL列表
     * @param url
     * @return
     */
	public static List<String> getUrlList(String url){
		List<String> urls = new ArrayList<String>();
		Document doc = GetDoc.getDoc(url);
		Elements  urlHtml  = doc.select("a.review-product");
		for(Element e : urlHtml){
			String OriginalUrl = "https://www.beautypedia.com/";
			urls.add(OriginalUrl+e.attr("href"));
		}
		return urls;
	}
	/**
	 * 通过url获得产品详细信息
	 * @param url
	 * @return
	 */
	public static BeautyProduct GetProductDetail(String url){
		Document doc = GetDoc.getDoc(url);
		BeautyProduct beautyProduct = new BeautyProduct();
		Elements  name  = doc.select("a.product-name");
		beautyProduct.setName(name.text());		
		Elements  price  = doc.select("span.price");
		beautyProduct.setPrice(Float.parseFloat(price.text()));
		Elements  rating  = doc.select("span.rating");
		String stars = rating.toString();
		int CommunityRating = 0;
		int ExpertRating = 0;
		int firstSpan =  stars.indexOf("<span");
		int lastSpan = stars.lastIndexOf("<span");
		char ee = stars.charAt(26);
		ExpertRating = Character.getNumericValue(ee)+1;
		if(firstSpan != lastSpan){
			char cc =stars.charAt(stars.lastIndexOf("<span")+26);
			CommunityRating = Character.getNumericValue(cc)+1;
		}
		beautyProduct.setCommunityRating(CommunityRating);
		beautyProduct.setExpertRating(ExpertRating);
		Elements  EXPERTREVIEWS  = doc.select("div.content-item,expert");
		beautyProduct.setEXPERTREVIEWS(EXPERTREVIEWS.get(0).text());
		beautyProduct.setCOMMUNITYREVIEWS(EXPERTREVIEWS.get(1).text());
		beautyProduct.setCLAIMS(EXPERTREVIEWS.get(2).text());
		String INGREDIENTSs = EXPERTREVIEWS.get(3).text();
		String[] INGREDIENTa = INGREDIENTSs.split(",");
		List<String> INGREDIENT = new ArrayList<>();
		for(String s:INGREDIENTa){
			INGREDIENT.add(s);
		}
		beautyProduct.setINGREDIENTS(INGREDIENT);
		beautyProduct.setBRANDOVERVIEW(EXPERTREVIEWS.get(4).text());
		beautyProduct.toString();
        return beautyProduct;
	}
	/**
	 * 获得翻页后的url
	 * @param page
	 * @return
	 */
	public static String getPageUrl ( String url, int page){
		String u = "?N=4294966879+4294942960&No=";
		int r = 24+(page-2)*24;
		String l = "&Nrpp=24&Ns=p_num_days_published%7C0";
        String FinalUrl =url+u+r+l;
        return FinalUrl;
	}
	/**
	 * 根据类别获取页数
	 * @param
	 * @param
	 * @return
	 */
	public static int GetPage(String url){
    	Document doc = GetDoc.getDoc(url);
    	Elements e = doc.select("span.total-pages");
    	int page = Integer.parseInt(e.text());
    	return page;
	}
	/**
	 * 获得所有目录对象
	 * @return
	 */
	public static List<BeautyCategory> GetCategory(){
		int id = 0;
    	List<BeautyCategory > beautyCategorys = new ArrayList();    	
	    String url = "https://www.beautypedia.com";
    	Document doc = GetDoc.getDoc(url);
    	Elements e = doc.select("div.menu-item,menu-skincare");
    	for(int y=1 ; y<6 ; y=y+2){
        	Type types = BeautyCategory.Type.SKINCARE ;
        	switch (y) {
    		case 1:
    			types=BeautyCategory.Type.SKINCARE;
    			break;
    		case 3:
    			types=BeautyCategory.Type.MAKEUP;
    			break;
    		case 5:
    			types=BeautyCategory.Type.BESTPRODUCTS;
    			break;
    		default:
    			break;
    		}
    		Elements cc = e.get(y).select("div.column");
    		for(int i=0 ; i<cc.size();i++){
    			Elements ccc = cc.get(i).select("a.submenu-item");
    			for(Element ee : ccc){
    				id++;
    				BeautyCategory beautyCategory = new BeautyCategory();
					beautyCategory.setCategoryId(id);
    				beautyCategory.setCategoryName(ee.text());
    				beautyCategory.setUrl("https://www.beautypedia.com/"+ee.attr("href"));
    				beautyCategory.setType(types);
    				beautyCategorys.add(beautyCategory);
    			}
    		}
    		
    	}
    	return beautyCategorys;
	}
	
//	public static void saveGetCategoryAll(){
//		List<BeautyCategory> beautyCategorys = GetCategory();
//		for(BeautyCategory b :beautyCategorys){
//			GetConnect.insertCategory(b);
//		}
//	}
}

