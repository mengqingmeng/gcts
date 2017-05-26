package com.example.scrapy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
//        Result result=null;
//        try{
//            result = UnirestUtil.ajaxPostStr(searchUrl,"tableId=69&State=1&tableName=TABLE69&curstart="+pageIndex);
//        }catch (Exception e){
//            logger.info("JKScrapy ajaxPostStr exception");
//        }
            String fileName = "JK"+ DateUtil.simpleDate()+".xlsx";
        String os = System.getProperty("os.name");
        boolean osIsMacOsX = false;
        boolean osIsWindows = false;
        if (os != null) {
        os = os.toLowerCase();
            osIsMacOsX = "mac os x".equals(os);
            osIsWindows = os != null && os.indexOf("windows") != -1;
        }else{
            osIsWindows = true;
        }

        if (osIsMacOsX){
            fileName = "/Users/mqm/HBSData/"+fileName;
        }

        if(osIsWindows){
            fileName = "C:/HBSData/"+fileName;
        }
        Document doc = Jsoup.connect(searchUrl+"?"+"tableId=69&State=1&tableName=TABLE69&curstart="+pageIndex).post();
//            String data = (String) result.getData();
//            Document doc = Jsoup.parse(data);
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
            ReadAndWritePoiUtil pu = ReadAndWritePoiUtil.getInstance(fileName);
            pu.writeProuctInfo(products);
            logger.info("*****成功爬取进口库第"+pageIndex+"页");
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
        //产品技术要求页面内容
        String detailResult = getDetail(techDetailUrl);

        JSONArray pfcf = new JSONArray();//配方成分详情
        List<String> chengfen = new ArrayList<String>();//成分
        String scgy = "";//生产工艺
        String felling ="";//感官指标
        JSONArray wshxzb = new JSONArray();//卫生化学指标
        JSONArray wswzb = new JSONArray();//微生物指标
        JSONArray jyff = new JSONArray();//检验方法
        JSONObject syff = new JSONObject();//使用方法
        String cctj = "";//储存条件
        String bzq = "";//保质期

        if (!detailResult.isEmpty()){
            Document detailaDoc = Jsoup.parse(detailResult);

            //1.解析成分
            Element cfTitle = detailaDoc.getElementsContainingOwnText("原料中文名称").get(0);
            Element cfTotal = cfTitle.parent().parent();
            Elements cfTrs = cfTotal.getElementsByTag("tr");
            String sessionMuDi="";//缓存的成分使用目的
            for (int i=1;i<cfTrs.size();i++) {
                JSONObject cf = new JSONObject();
                Elements tds = cfTrs.get(i).children();

                if (tds.size()==3){
                    cf.put("原料中文名称",tds.get(1).text());
                    sessionMuDi = tds.get(2).text();
                    cf.put("使用目的",tds.get(2).text());
                    chengfen.add(tds.get(1).text());
                }else if(tds.size()==1){
                    cf.put("原料中文名称",tds.get(0).text());
                    cf.put("使用目的",sessionMuDi);
                    chengfen.add(tds.get(0).text());
                }

                pfcf.add(cf);
            }

            //2.解析生产工艺
            Element scgyTitle = detailaDoc.getElementsContainingOwnText("生产工艺").get(0);
            Element scqyTr = scgyTitle.parent().nextElementSibling();
            scgy= scqyTr.child(0).text();

            //3.解析感官指标
            Element ggzbTitle = detailaDoc.getElementsContainingOwnText("感官指标").get(0);
            felling = ggzbTitle.parent().nextElementSibling().text();

            //4.解析卫生化学指标
            Element wshxzbTitle = detailaDoc.getElementsContainingOwnText("【卫生化学指标】").get(0);
            Element wshxzbTable = wshxzbTitle.parent().nextElementSibling().getElementsByTag("table").get(0);
            Elements wshxzbTrs= wshxzbTable.getElementsByTag("tr");
            for (int i=1;i<wshxzbTrs.size();i++){
                JSONObject jo = new JSONObject();
                Elements wshxzbTds = wshxzbTrs.get(i).getElementsByTag("td");
                String jcxm = wshxzbTds.get(0).text();
                String zb = wshxzbTds.get(1).text();
                jo.put("检查项目",jcxm);
                jo.put("指标",zb);
                wshxzb.add(jo);
            }

            //5.解析微生物指标
            Element wswzbTitle = detailaDoc.getElementsContainingOwnText("【微生物指标】").get(0);
            Element wswTable = wswzbTitle.parent().nextElementSibling().getElementsByTag("table").get(0);
            Elements wswTrs = wswTable.getElementsByTag("tr");
            for (int i=1;i<wswTrs.size();i++){
                Elements wswTds = wswTrs.get(i).getElementsByTag("td");
                String jcxm = wswTds.get(0).text();
                String zb = wswTds.get(1).text();
                JSONObject jo = new JSONObject();
                jo.put("检查项目",jcxm);
                jo.put("指标",zb);
                wswzb.add(jo);
            }

            //6.解析检验方法
            Element jcffTitle = detailaDoc.getElementsContainingOwnText("【检验方法】").get(0);
            Element jcffTable = jcffTitle.parent().nextElementSibling().getElementsByTag("table").get(1);
            Elements jcffTrs = jcffTable.getElementsByTag("tr");
            JSONObject jyffJO = null;//检验指标+内容和方法对象数组
            JSONArray itemAndMethods = null;//内容和方法对象数组
            for (int i =1;i<jcffTrs.size();i++){
                Element jcffTr = jcffTrs.get(i);
                Elements jcffTds = jcffTr.getElementsByTag("td");
                String jcnr = "";
                String jcff="";
                boolean isNew =false;
                if (jcffTds.size()==3){
                    isNew=true;
                    jyffJO = new JSONObject();
                    itemAndMethods = new JSONArray();
                    jyffJO.put("检验指标",jcffTds.get(0).text());

                    jcnr=jcffTds.get(1).text();//检验项目
                    jcff=jcffTds.get(2).text();//检验方法
                    JSONObject itemAndMethod = new JSONObject();
                    itemAndMethod.put("检验项目",jcnr);
                    itemAndMethod.put("检验方法",jcff);

                    itemAndMethods.add(itemAndMethod);
                    jyffJO.put("检验项目和方法",itemAndMethods);

                }else{
                    jcnr=jcffTds.get(0).text();
                    jcff=jcffTds.get(1).text();
                    itemAndMethods = jyffJO.getJSONArray("检验项目和方法");
                    JSONObject itemAndMethod = new JSONObject();
                    itemAndMethod.put("检验项目",jcnr);
                    itemAndMethod.put("检验方法",jcff);

                    itemAndMethods.add(itemAndMethod);

                }

                if (isNew){
                    jyff.add(jyffJO);
                }
            }

            //7.解析使用方法
            Element syffTitle = detailaDoc.getElementsContainingOwnText("【使用方法】").get(0).parent();
            String useage = syffTitle.nextElementSibling().getElementsByTag("td").get(0).text();
            String attation = syffTitle.nextElementSibling().nextElementSibling().nextElementSibling().getElementsByTag("td").get(0).text();
            syff.put("使用方法",useage);
            syff.put("注意事项",attation);

            //8.解析贮存条件
            Element cctjTitle = detailaDoc.getElementsContainingOwnText("【贮存条件】").get(0).parent();
            cctj = cctjTitle.nextElementSibling().getElementsByTag("td").get(0).text();

            //9.解析保质期
            Element bzqTitle = detailaDoc.getElementsContainingOwnText("【保质期】").get(0).parent();
            bzq =bzqTitle.nextElementSibling().getElementsByTag("td").get(0).text();

        }

        jkProduct.setChName(chName);
        jkProduct.setEngName(engName);
        jkProduct.setApplyState(applyState);
        jkProduct.setClazz(clazz);
        jkProduct.setFromCountry(fromCountry);
        jkProduct.setFromChEnterprise(fromChEnterprise);
        jkProduct.setFromEngEnterprise(fromEngEnterprise);
        jkProduct.setSjscqyAddress(sjscqyAddress);
        jkProduct.setZhsbzrdw(zhsbzrdw);
        jkProduct.setZhsbzraddress(zhsbzraddress);
        jkProduct.setApplyNumber(applyNumber);
        jkProduct.setApplyDate(applyDate);
        jkProduct.setApplyEffective(applyEffective);
        jkProduct.setMarkInfo(mark);
        jkProduct.setProductMark(productMark);
        jkProduct.setZhu(zhu);
        jkProduct.setProductionProcess(scgy);
        jkProduct.setFelling(felling);
        jkProduct.setWeishenghuaxue(wshxzb.toJSONString());
        jkProduct.setWeishengwu(wswzb.toJSONString());
        jkProduct.setJianyanfangfa(jyff.toJSONString());
        jkProduct.setShiyongfangfa(syff.toJSONString());
        jkProduct.setChucuntiaojian(cctj);
        jkProduct.setBaozhiqi(bzq);
        jkProduct.setInfoMations(chengfen);
        jkProduct.setInfoMationsDetail(pfcf.toJSONString());
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
        Connection.Response response = conn.execute();
        byte[] data = response.bodyAsBytes();
        InputStream is = new ByteArrayInputStream(data);
        BufferedImage grayImage = ImageHelper.convertImageToBinary(ImageIO.read(is));
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        File directory = new File("");
        instance.setDatapath(getClass().getResource(".").getFile().toString());

        instance.setLanguage("eng");
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
