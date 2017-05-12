package com.example.amazonSprider;

import com.example.entity.AmazonProduct;
import com.example.util.AmazonSpiderUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 陈成 on 2017/5/12.
 */
@Component
public class AmazonUrlSprider {
    @Autowired
    private AmazonSpiderUtil su ;
    private static String  amazon = "https://www.amazon.com";


    public Map<String,String> getUrls(String url) throws Exception{

        Map<String ,String> urls = new HashMap<String, String>();
        Document doc = su.getDocument(url);
        Elements es = doc.getElementsByClass("browseBox");
        Iterator<Element> i = es.iterator();
        Element e = null;
        if(i.hasNext()){
            e = i.next();
            es = e.getElementsByTag("a");
            i = es.iterator();
            while(i.hasNext()){
                e = i.next();
                urls.put(e.html(),amazon +e.attr("href"));
            }
        }
        return urls;
    }

    public int getPageCount(Document doc){
        Iterator<Element> i = doc.getElementById("search-results").getElementsByClass("pagnDisabled").iterator();

        if(i.hasNext()){
            return	 Integer.parseInt(i.next().html());

        }
        return 0 ;
    }

    public Document getNextPage(Document doc){

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

    public void saveProductUrl(Document doc){
        Element e = null;
        Elements es = null ;
        Iterator<Element> i ;
        AmazonProduct product = null;
        String s = "";
        es = doc.getElementById("search-results").getElementsByClass("s-access-detail-page");
        i = es.iterator();
        while(i.hasNext()){
            e = i.next();
            product = new AmazonProduct();
            s = e.attr("href");
            if(!s.contains(amazon)){
                s = amazon + s;
            }
            product.setUrl(s);
          //  productDao.saveProductUrl(product);
        }
    }
    public void saveProductUrl(Document doc,int m ){

        Element e = null;
        Elements es = null ;
        Iterator<Element> i ;
        AmazonProduct product = null;
        String s = "";
        es = doc.getElementById("s-results-list-atf").getElementsByClass("s-access-detail-page");
        i = es.iterator();
        while(i.hasNext()){
            e = i.next();
            product = new AmazonProduct();
            s = e.attr("href");
            if(!s.contains(amazon)){
                s = amazon + s;
            }
            product.setUrl(s);
           // productDao.saveProductUrl(product);
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
}
