package com.example.scrapy;

import com.example.entity.JKProduct;
import com.example.result.Result;
import com.example.util.DateUtil;
import com.example.util.ReadAndWritePoiUtil;
import com.example.util.UnirestUtil;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mqm on 2017/5/23.
 */
@Component
public class JKScrapy {
    private final static Logger logger = LoggerFactory.getLogger(JKScrapy.class);

    public void jk(Integer pageIndex) throws Exception {
        String baseUrl = "http://app1.sfda.gov.cn/datasearch/face3/";
        String searchUrl = baseUrl+"search.jsp";
        //产品名称列表
        Result result=null;
        try{
            result = UnirestUtil.ajaxPostStr(searchUrl,"tableId=69&State=1&tableName=TABLE69&curstart="+pageIndex);
        }catch (Exception e){
            logger.info("JKScrapy ajaxPostStr exception");
        }
        if (result.getCode()==200 && result.getMessage().equals("OK")){
            String fileName = "JK"+ DateUtil.simpleDate()+".xlsx";

            String data = (String) result.getData();
            Document doc = Jsoup.parse(data);
            Elements as = doc.getElementsByTag("a");
            List<JKProduct> products = new ArrayList<JKProduct>();//产品信息封装
            for (Element element:as) {
                String hrefValue = element.attr("href");
                String[] hrefs = hrefValue.split("'");
                String[] urlAndParams = hrefs[1].split("\\?");
                JKProduct product = getProduct(baseUrl+urlAndParams[0],urlAndParams[1]);
                products.add(product);
            }

            //将数据写入excel中，每页写一次
            //ReadAndWritePoiUtil pu = ReadAndWritePoiUtil.getInstance(fileName);
            //pu.writeProuctInfo(products);
        }
    }

    /**
     * 获取产品内容
     * @param url
     * @param params
     * @return
     * @throws UnirestException
     * @throws IOException
     */
    public JKProduct getProduct(String url,String params) throws UnirestException, IOException {
        JKProduct jkProduct = new JKProduct();
        //产品信息页面
        Map<String,String> cookies = null;
        Connection conn = Jsoup.connect(url+"?"+params).timeout(10000);
        Document contentDoc = conn.post();
        Connection.Response response = conn.response();
        cookies = response.cookies();
        String body = response.body();

        Elements contentTrs=contentDoc.getElementsByTag("tr");
        if (contentTrs.size()<1){
            return null;
        }
        String chName = contentTrs.get(1).getElementsByTag("td").get(1).text();//中文名
        String applyState=contentTrs.get(2).getElementsByTag("td").get(1).text();//备案状态
        String engName=contentTrs.get(3).getElementsByTag("td").get(1).text();//英文名
        String clazz=contentTrs.get(4).getElementsByTag("td").get(1).text();//产品类别
        String fromCountry=contentTrs.get(5).getElementsByTag("td").get(1).text();//出产国
        String fromChEnterprise=contentTrs.get(6).getElementsByTag("td").get(1).text();//生产企业（中文）
        String fromEngEnterprise=contentTrs.get(7).getElementsByTag("td").get(1).text();//生产企业（英文）
        String sjscqyAddress=contentTrs.get(8).getElementsByTag("td").get(1).text();//生产企业地址
        String zhsbzrdw=contentTrs.get(9).getElementsByTag("td").get(1).text();//在华申报责任单位
        String zhsbzraddress=contentTrs.get(10).getElementsByTag("td").get(1).text();//在华责任单位地址
        String applyNumber=contentTrs.get(11).getElementsByTag("td").get(1).text();//批准文号
        String applyDate=contentTrs.get(12).getElementsByTag("td").get(1).text();//批准日期
        String applyEffective=contentTrs.get(13).getElementsByTag("td").get(1).text();//批准有效期
        String mark=contentTrs.get(14).getElementsByTag("td").get(1).text();//备注
        String productMark=contentTrs.get(15).getElementsByTag("td").get(1).text();//产品名称备注
        String zhu=contentTrs.get(17).getElementsByTag("td").get(1).text();//注

        String techDetailUrl =contentTrs.get(16).getElementsByTag("td").get(1)
                .getElementsByTag("a").get(0).attr("href");//产品技术要求，详细内容地址

        logger.info("chname:"+chName);
        //产品技术要求
        String detailResult = getDetail(techDetailUrl);
        logger.info("detailResult:"+detailResult);

        return jkProduct;
    }

    /**
     * 获取技术要求
     * @param url
     * @return
     */
    public String getDetail(String url) throws UnirestException, IOException {
        Map<String, String> cookies = new HashMap<String,String>();
        Connection conn = null;
        Connection.Response response = null;

        conn = Jsoup.connect("http://123.127.80.6/sfda/ShowJSYQAction.do");
        response = conn.execute();
        cookies =response.cookies();

        String code = getVeriCode(cookies);

       conn =  Jsoup.connect(url+"&randomInt="+code+"&process=showNew").timeout(10000).cookies(cookies);
        //conn =  Jsoup.connect("http://123.127.80.6/sfda/ShowJSYQAction.do?"+"PID=4a7f362c3aff91c0d83b05d526e71e9e&randomInt="+code+"&process=showNew").timeout(10000).cookies(cookies);
        response = conn.execute();
        String detailStr=response.body();
        //logger.info("xxxx:"+detailStr);
        if (detailStr==null ||detailStr.isEmpty()||detailStr .indexOf("alert('验证码错误，请重新输入！');")!=-1){
            int count = 10;
            while (count>0){

                conn = Jsoup.connect("http://123.127.80.6/sfda/ShowJSYQAction.do");
                response = conn.execute();

                cookies =response.cookies();
                code = getVeriCode(cookies);

                conn = Jsoup.connect(url+"&randomInt="+code+"&process=showNew").timeout(10000).cookies(cookies);
                conn.post();
                response = conn.execute();
                detailStr=response.body();
                if (detailStr!=null && detailStr.indexOf("alert('验证码错误，请重新输入！');")==-1){
                    return detailStr;
                }else if (detailStr.indexOf("该产品无此信息")!=-1){
                    detailStr ="";
                    return detailStr;
                }
                count--;
            }

        }else if(detailStr.indexOf("该产品无此信息")!=-1){
            detailStr ="";
        }
        return detailStr;
    }

    /**
     * 获取验证码
     * @return
     * @throws UnirestException
     * @throws IOException
     */
    public String getVeriCode(Map<String,String> cookies) throws UnirestException, IOException {
        if (cookies==null)
            cookies=getCookies();
        String urlPath = "http://123.127.80.6/servlet/GetImageServlet?sn=randomImage";
        Connection conn = Jsoup.connect(urlPath).timeout(10000).cookies(cookies);
        conn.post();
        Connection.Response response = conn.execute();
        byte[] data = response.bodyAsBytes();
        InputStream is = new ByteArrayInputStream(data);
        BufferedImage grayImage = ImageHelper.convertImageToBinary(ImageIO.read(is));
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        String code ="";
        try {
            code = instance.doOCR(grayImage).substring(0,4);
        } catch (TesseractException e) {
            e.getLocalizedMessage();
        }
        return code;
    }

    public Map<String,String> getCookies() throws IOException {
        Connection conn = Jsoup.connect("http://123.127.80.6/sfda/ShowJSYQAction.do");
        Connection.Response response = conn.execute();
        return response.cookies();
    }


}
