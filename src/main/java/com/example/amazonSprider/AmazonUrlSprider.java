package com.example.amazonSprider;

import com.example.entity.AmazonProduct;
import com.example.entity.AmazonProductUrl;
import com.example.util.AmazonSpiderUtil;
import com.example.util.DateUtil;
import com.example.util.ReadAndWritePoiUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by 陈成 on 2017/5/12.
 */
@Component
public class AmazonUrlSprider {

    private AmazonSpiderUtil su =new AmazonSpiderUtil();
    private static final String  amazon = "https://www.amazon.com";


    public Map<String,Map<String,String>> getAllUrls(String url) throws Exception{
        Map<String,Map<String,String>> allUrls = new HashMap<>();
        Map<String ,String> urls = null;
        Document doc = su.getDocument(url);
        Elements es = doc.getElementsByClass("browseBox");
        Iterator<Element> i = es.iterator();
        Element e = null;
        Element next = null;
        Elements li = null;
        String title = null;
        if(i.hasNext()){
            e = i.next();
            es = e.getElementsByTag("h3");
            i = es.iterator();
            while(i.hasNext()){
                urls = new HashMap<>();
                e = i.next();
                title = e.text();
                next = e.nextElementSibling();
                li = next.getElementsByTag("a");
                Iterator<Element> j = li.iterator();
                while (j.hasNext()){
                    next = j.next();
                    urls.put(next.text(),amazon +next.attr("href"));
                }
                allUrls.put(title,urls);
            }
        }
        return allUrls;
    }

    public Map<String,String> getUrls(String url) throws Exception{
        Map<String ,String> urls = new HashMap<>();
        Document doc = su.getDocument(url);
        Elements es = doc.getElementsByClass("categoryRefinementsSection");
        Iterator<Element> i = es.iterator();
        Element e = null;
        Element next = null;
        Elements li = null;
        String url1 = null;
        String title = null;
        if(i.hasNext()){
            e = i.next();
            es = e.getElementsByTag("a");
            i = es.iterator();

            while(i.hasNext()){
                title = "";
                e = i.next();
                url1 = amazon +e.attr("href");
                li = e.getElementsByTag("span");
                Iterator<Element> j = li.iterator();
                while (j.hasNext()){
                    next = j.next();
                   title += next.text();
                }
                urls.put(title,url1);
            }
        }
        return urls;
    }







    private int getPageCount(Document doc){
        Iterator<Element> i = doc.getElementById("search-results").getElementsByClass("pagnDisabled").iterator();

        if(i.hasNext()){
            return	 Integer.parseInt(i.next().html());

        }
        return 0 ;
    }

    private Document getNextPage(Document doc){

        String nextUrl = doc.getElementById("pagnNextLink").attr("href");
        String str = amazon+nextUrl;
        System.out.println(str);
        try {
            return Jsoup.parse(su.getURLSource(str));
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    private List<AmazonProductUrl> saveProductUrl(Document doc){
        Element e = null;
        Elements es = null ;
        Iterator<Element> i ;
        AmazonProductUrl product = null;
        List<AmazonProductUrl> urls = new ArrayList<AmazonProductUrl>();
        String s = "";
        es = doc.getElementById("search-results").getElementsByClass("s-access-detail-page");
        i = es.iterator();
        while(i.hasNext()){
            e = i.next();
            product = new AmazonProductUrl();
            s = e.attr("href");
            if(!s.contains(amazon)){
                s = amazon + s;
            }
            product.setUrl(s);
            urls.add(product);
        }
        return urls;
    }
    private List<AmazonProductUrl> saveProductUrl(Document doc,int m ){

        Element e = null;
        Elements es = null ;
        Iterator<Element> i ;
        AmazonProductUrl product = null;
        String s = "";
        List<AmazonProductUrl> urls = new ArrayList<AmazonProductUrl>();
        es = doc.getElementById("s-results-list-atf").getElementsByClass("s-access-detail-page");
        i = es.iterator();
        while(i.hasNext()){
            e = i.next();
            product = new AmazonProductUrl();
            s = e.attr("href");
            if(!s.contains(amazon)){
                s = amazon + s;
            }
            product.setUrl(s);
            urls.add(product);
        }
        return urls;
    }

/*
    public void startAmazonUrlSprider(String url,String clazz){
        Document doc = null;
        Document temp = null;
        int pageCount;
        List<AmazonProductUrl> urls = new ArrayList<AmazonProductUrl>();
        try {
            doc = su.getDocument(getUrls(url).get(clazz));
            pageCount =getPageCount(doc);
            urls.addAll(saveProductUrl(doc));
            ReadAndWritePoiUtil pu = new ReadAndWritePoiUtil("d:/Data/Amazon/Url.xls");
            for(AmazonProductUrl u:urls) {
                pu.writeProuctInfo(u);
            }
            int i = 0 ;
            for(int m = 1 ; m  < pageCount ; m++) {

                //doc = Jsoup.parse(sm.getNextPage(doc));
                urls.clear();
                temp = getNextPage(doc);
                if (temp != null) {
                    doc = temp;
                    urls.addAll(saveProductUrl(doc, m));
                } else {
                    m--;
                }



                for(AmazonProductUrl u:urls) {
                    pu.writeProuctInfo(u);
                }
                System.out.println(m);
            }

        } catch (Exception e1) {

            e1.printStackTrace();
        }

    }
    }
*/


    public void startAmazonUrlSprider(String url){
        Document doc = null;
        Document temp = null;
        AmazonProductSprider ps = null;
        int pageCount;
        List<AmazonProductUrl> urls = new ArrayList<>();
        ReadAndWritePoiUtil rw = null;
        String fileName = "JK" + DateUtil.simpleDate() + ".xlsx";
        String os = System.getProperty("os.name");
        boolean osIsMacOsX = false;
        boolean osIsWindows = false;
        if (os != null) {
            os = os.toLowerCase();
            osIsMacOsX = "mac os x".equals(os);
            osIsWindows = os != null && os.indexOf("windows") != -1;
        } else {
            osIsWindows = true;
        }

        if (osIsMacOsX) {
            fileName = "/Users/mqm/HBSData/" + fileName;
        }

        if (osIsWindows) {
            fileName = "C:/HBSData/" + fileName;
        }
        try {
            rw = ReadAndWritePoiUtil.getInstance(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("创建excel 失败");
            e.printStackTrace();
        }
        List<AmazonProduct> products = new ArrayList<>();
        try {
            ps = new AmazonProductSprider();
            doc = su.getDocument(url);
            pageCount =getPageCount(doc);
            urls.addAll(saveProductUrl(doc));
         //   ReadAndWritePoiUtil pu = new ReadAndWritePoiUtil("d:/Data/Amazon/Url.xls");
                ps.startAmazonProductSprider(urls);
          //  int i = 0 ;
            for(int m = 1 ; m  < pageCount ; m++) {

                //doc = Jsoup.parse(sm.getNextPage(doc));
                urls.clear();
                temp = getNextPage(doc);
                if (temp != null) {
                    doc = temp;
                    urls.addAll(saveProductUrl(doc, m));
                } else {
                    m--;
                }

                products.addAll( ps.startAmazonProductSprider(urls));
                if(products.size()>100){
                    rw.writeProuctInfo(products);
                    products.clear();
                }
                System.out.println(m);
            }
                if(products.size()>0){
                    rw.writeProuctInfo(products);
                    products.clear();
            }
        } catch (Exception e1) {

            e1.printStackTrace();
        }

    }
}


  /*  public static void main(String[] args) throws Exception {

*//*	String str = 	"https://www.amazon.com/s/ref=lp_11060711_pg_2/144-9495038-4173818?rh=n%3A3760911%2Cn%3A%2111055981%2Cn%3A11060451%2Cn%3A11060711&page=2&ie=UTF8&qid=1494410268&spIA=B00Z75ZDAU,B00QUKS6NW,B00G2TQNZ4";

	File f = new File("D:\\file.html");
	Writer w =  new FileWriter(f);

	w.write(su.getURLSource(str));


	}*//*
        String url = "https://www.amazon.com/Beauty-Makeup-Skin-Hair-Products/b/ref=nav_shopall_bty?ie=UTF8&node=3760911";

        SpiderMain sm = new SpiderMain();
        Document doc = null;
        Document temp = null;
        int pageCount;

        try {
            doc = su.getDocument(sm.getUrls(url).get("Face"));
            pageCount = sm.getPageCount(doc);

            sm .saveProductUrl(doc);
            for(int m = 1 ; m  < pageCount ; m++){

                //doc = Jsoup.parse(sm.getNextPage(doc));

                temp = sm.getNextPage(doc);
                if(temp!=null){
                    doc = temp;
                }else{
                    m--;
                }
                sm .saveProductUrl(doc ,m);
                System.out.println(m);
            }


        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }
*/

