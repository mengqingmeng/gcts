package com.example.amazonSprider;

import com.example.entity.AmazonProduct;
import com.example.entity.AmazonProductInfoMation;
import com.example.util.AmazonSpiderUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 陈成 on 2017/5/12.
 */
public class AmazonProductSprider {

    @Autowired
    private static AmazonSpiderUtil su ;
    private Document doc;

    public AmazonProductSprider(String url) {
        //this.doc = Jsoup.parse(su.getURLSource(url));
        try {
            this.doc = su.getDocument(url);
        } catch (Exception e) {
            this.doc = null;
            e.printStackTrace();
        }

    }


    public String getProductName(){

        Element e = doc.getElementById("productTitle");
        return e.html();
    }


    public String getProductMark(){
        Element e = doc.getElementById("acrPopover");
        return e.attr("title").substring(0,3);


    }

    public String getCountReviews(){
        Element e = doc.getElementById("acrCustomerReviewText");
        String str = e.html();
        return getNum(str);

    }


    public String getCountAnswers(){
        Element e = doc.getElementById("askATFLink");
        if(e != null){
            e = e.getElementsByClass("a-size-base").first();
            return getNum(e.html());
        }else{
            return null;
        }

    }

    private String  getNum(String str){
        String reg = "[^0-9]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    public String getSaleWell() {
        Element e = doc.getElementById("zeitgeistBadge_feature_div");
        if(e!=null){
            Elements es = e.getElementsByClass("rank-number");
            Elements es1 = e.getElementsByClass("cat-link");
            String str = null;
            if(es.size()>0){

                str = es.first().html()+"in" + es1.first().html();
            }

            return str;
        }
        return null;
    }


    public String getAoubtProduct(){
        String str = "";
        Element e = doc.getElementById("fbExpandableSectionContent");
        if(e!=null){
            Elements es = e.getElementsByClass("a-list-item");

            Iterator<Element> i = es.iterator();
            while(i.hasNext()){
                str += i.next().html();
            }
        }
        return str ;
    }


    public String getPrice(){
        Element e = doc.getElementById("priceblock_ourprice");
        if(e != null){
            return e.html();
        }else{
            e = doc.getElementById("priceblock_saleprice");
            return e.html();
        }

    }

    public String getSize(){
        Element e = doc.getElementById("productDescription");
        if(e!=null){
            Elements es = e.getElementsByTag("strong");
            if(es!=null&&es.size()>0)
                return es.first().html();
        }
        return null;
    }

    public String getDescription(){

        Element e = doc.getElementById("productDescription");
        if(e!=null)
            return e.getElementsByTag("p").first().html();
        return null;
    }


    public String getDetalis(){
        String str = "";
        Element e = doc.getElementById("detail-bullets");
        if(e != null){
            Elements disclaim = e.getElementsByClass("disclaim");
            Elements li = e.getElementsByTag("li");

            if(disclaim.first() != null){
                str = "size:" +disclaim.first().getElementsByTag("strong").first().html();
            }
            Iterator<Element> i  = li.iterator();
            while(i.hasNext()){
                e = i.next();
                str += e.html();
                str = str.replaceAll("<li>", " ");
                str =str.replaceAll("<b>"," ");
                str =str.replaceAll("</b>"," ");
                str =str.replaceAll("</li>"," ");
                str = str .replaceAll("<script[a-zA-Z0-9\\s\\D]*</script>", " ");
                str = str.replaceAll("<ul [a-zA-Z0-9\\s\\D]*> ", "").replaceAll("<li class=\"zg_hrsr_item\">", "")
                        .replaceAll("<span[a-zA-Z0-9\\s\\D]*>", "").replaceAll("</span>", "").replaceAll("&nbsp"," ").replace("&gt"," ").replaceAll("<a [a-zA-Z0-9\\s\\D]*>", " ")
                        .replaceAll("</a>", " ");

            }}
        return str;

    }


    public List<AmazonProductInfoMation> getInformation(AmazonProduct p){
        List<AmazonProductInfoMation> infos =  null;
        AmazonProductInfoMation info = null;

        String string = null;
        Element e = doc.getElementById("importantInformation");
        if(e != null){
            infos = new ArrayList<AmazonProductInfoMation>();
            e=e.getElementsByClass("content").first();
            string = e.html().replaceAll(e.getElementsByTag("h5").first().toString(), "");
            String[] strarr = string.split(":");
            for(String s : strarr){
                String [] strarr1 = s.split(",");
                for(String s1 : strarr1){
                   /* info = productDao.getInfoByName(s1);
                    if(info == null){
                        info = new Information();
                        List<Product> products = new ArrayList<Product>();
                        info .setProducts(products);
                        info .setName(s1);
                    }

                    info.getProducts().add(p);
                    infos.add(info);*/
                }
            }

        }
        return infos;

    }

/*
    public static void main(String[] args) {
        ProductSpider ps = null ;
        String url = null;
        List<Product> products = productDao.getProducts();
        int i = 0 ;
        try {
            for(Product p : products){
                if(p.getName() ==null){
                    url = p.getUrl();
                    System.out.println(url);
                    int  c =  1;
                    do{
                        System.out.println("获取doc"+ c++ +"次");
                        ps = new ProductSpider(url);
                        if(c > 10){
                            break;
                        }
                    }while(ps.getDoc() == null);
                    if(ps.getDoc() == null)
                        continue;

                    p.setName(su.getUtf_8(ps.getProductName()));
                    p.setMark(ps.getProductMark());
                    p.setAboutThisProduct(ps.getAoubtProduct());
                    p.setCountAnswers(ps.getCountAnswers());
                    p.setCountReviews(ps.getCountReviews());
                    p.setDescription(ps.getDescription());
                    p.setDetails(ps.getDetalis());
                    p.setInformations(ps.getInformation(p));
                    p.setPrice(ps.getPrice());
                    p.setSaleWell(ps.getSaleWell());
                    p.setSize(ps.getSize());
                    productDao.saveProduct(p);

                }else{
                    System.out.println("此产品已经获取过");
                }
            }
            System.out.println(i++);
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }

*/

    public Document getDoc() {
        return doc;
    }


    public void setDoc(Document doc) {
        this.doc = doc;
    }
}
