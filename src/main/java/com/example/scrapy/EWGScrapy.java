package com.example.scrapy;

import com.example.entity.EWGProduct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mqm on 2017/5/17.
 */
@Component
public class EWGScrapy {

    public String scrapy(String type,Integer fromPage,Integer toPage) throws Exception {
        for(int i=fromPage;i<=toPage;i++){
            String url = "http://www.ewg.org/skindeep/browse.php?category="+ URLEncoder.encode(type,"utf-8")+"&&showmore=products&start="+(i-1)*10;
            List<EWGProduct> products = scrapyOnePage(url);
        }
        return "";
    }

    public List<EWGProduct> scrapyOnePage(String url) throws IOException {
        Document productsDoc = Jsoup.connect(url).get();
        Elements products = productsDoc.getElementsByClass("product_name_list");
        List<EWGProduct> ewgProducts = new ArrayList<EWGProduct>();
        for (Element element :products) {
            Element product = element.child(0);
            EWGProduct ewgProduct = null;
            if (product != null) {
                ewgProduct = new EWGProduct();
                String productUrl = "https://www.ewg.org" + product.attr("href");
                Document productDoc = Jsoup.connect(productUrl).get();
                Element righttoptitleandcats = productDoc.getElementById("righttoptitleandcats");
                Element productNameH1 = righttoptitleandcats.getElementsByTag("h1").get(0);
                String productName = productNameH1.text();//产品名称
                String OverallHazard = "";
                String Cancer = "";
                String DevelopmentalAndReproductiveToxicity = "";
                String AllergiesAndImmunotoxicity = "";
                String UseRestrictions = "";

                Elements individualbars = productDoc.getElementsByClass("individualbar_3col");
                Element overallHazardElement = individualbars.get(0).getElementsByClass("basic_bar").get(0);
                Element CancerElement = individualbars.get(1).getElementsByClass("basic_bar").get(0);
                Element DevelopmentalAndReproductiveToxicityElement = individualbars.get(2).getElementsByClass("basic_bar").get(0);
                Element AllergiesAndImmunotoxicityElement = individualbars.get(3).getElementsByClass("basic_bar").get(0);
                Element UseRestrictionsElement = individualbars.get(4).getElementsByClass("basic_bar").get(0);
                OverallHazard = getNumOfStr(overallHazardElement.attr("style"));
                Cancer = getNumOfStr(CancerElement.attr("style"));
                DevelopmentalAndReproductiveToxicity = getNumOfStr(DevelopmentalAndReproductiveToxicityElement.attr("style"));
                AllergiesAndImmunotoxicity = getNumOfStr(AllergiesAndImmunotoxicityElement.attr("style"));
                UseRestrictions = getNumOfStr(UseRestrictionsElement.attr("style"));

                Elements chengFens = productDoc.getElementsByClass("firstcol");

                List<String> chengFenList = new ArrayList<String>();
                for (Element chengFen:chengFens) {
                    String chengFenName = chengFen.child(0).text();
                    String detailChengFen = chengFen.getElementsByTag("em").get(0).text();
                    chengFenList.add(chengFenName+"/"+detailChengFen);
                }

                ewgProduct.setProductName(productName);
                ewgProduct.setOverallHazard(OverallHazard);
                ewgProduct.setCancer(Cancer);
                ewgProduct.setDevelopmentalAndReproductiveToxicity(DevelopmentalAndReproductiveToxicity);
                ewgProduct.setAllergiesAndImmunotoxicity(AllergiesAndImmunotoxicity);
                ewgProduct.setUseRestrictions(UseRestrictions);
                ewgProduct.setInfoMations(chengFenList);
            }

            if (ewgProduct !=null)
                ewgProducts.add(ewgProduct);
        }

        return ewgProducts;
    }

    public String getNumOfStr(String str){
        str=str.trim();
        String newStr = "";
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)>=0 && str.charAt(i)<=57){
                newStr+=str.charAt(i);
            }
        }
        return newStr;
    }
}
