package com.example.scrapy;

import com.example.entity.AmazonProduct;
import com.example.entity.GCFTProduct;
import com.example.entity.JKProduct;
import com.example.result.Result;
import com.example.util.*;
import com.example.util.UnirestUtil;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mqm on 2017/5/11.
 */
@Component
public class GcftScrapy {
    @Autowired JKScrapy jkScrapy;


    private final static Logger logger = LoggerFactory.getLogger(GcftScrapy.class);

    int gcftLine = 0;

    public void Scrapy(Integer fromPage,Integer toPage,String ku) throws Exception {
        if (ku.equals("gcft")){//国产非特殊用途库
            for(int i=fromPage;i<=toPage;i++){
                logger.info("国产非特殊用途，正在爬取，第"+ i+"页");
                gcft(i);

            }
            logger.info("******国产非特殊用途爬取完成**********");
        }else if(ku.equals("jk")) {
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
            ReadAndWritePoiUtil pu = ReadAndWritePoiUtil.getInstance(fileName);

            for (int i = fromPage; i <= toPage; i++) {
                logger.info("进口化妆品库，正在爬取，第" + i + "页");
                jkScrapy.jk(i, pu);
            }
            logger.info("******进口化妆品库爬取完成**********");
        }

    }

    /**
     * 国产非特殊用途
     * @param pageIndex 页数
     * @throws Exception
     */
    public void gcft(Integer pageIndex) throws Exception {
        String url = "http://125.35.6.80:8080/ftba/itownet/fwAction.do?method=getBaNewInfoPage";

        Result result = UnirestUtil.ajaxPost(url,"pageSize=15&page="+pageIndex);
        if (result.getCode()==200 && result.getMessage().equals("OK")){
            JSONObject jsonObject = (JSONObject) result.getData();
            JSONArray productList = jsonObject.getJSONArray("list");

            List<GCFTProduct> productResult = new ArrayList<GCFTProduct>();
            for (int i = 0;i<productList.length();i++){
                JSONObject product = productList.getJSONObject(i);
                String processid = product.getString("processid");

                //获取详细信息的请求链接
                String detailUrl = "http://125.35.6.80:8080/ftba/itownet/fwAction.do?method=getBaInfo";
                String detailParams = "processid="+processid;
                Result detailResult = UnirestUtil.ajaxPost(detailUrl,detailParams);
                if (detailResult.getCode()==200 && detailResult.getMessage().equals("OK")){
                    JSONObject detail = (JSONObject) detailResult.getData();
                    GCFTProduct returnProduct = dealGcftDetail(detail);
                    productResult.add(returnProduct);
                }
            }

            //调用方法保存至excel中
            String fileName = "GCFT"+DateUtil.simpleDate()+".xlsx";
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

            ReadAndWritePoiUtil pu = ReadAndWritePoiUtil.getInstance(fileName);
            pu.writeProuctInfo(productResult);
        }
    }

    /**
     * 获取国产产品详细内容
     * @param detail
     * @return
     * @throws IOException
     */
    public GCFTProduct dealGcftDetail(JSONObject detail) throws IOException {
        JSONArray sjscqyList = detail.getJSONArray("sjscqyList");//实际生产企业
        JSONObject scqyUnitinfo = detail.getJSONObject("scqyUnitinfo");//生产企业单元信息
        JSONArray pfList = detail.getJSONArray("pfList");//成分列表

        String apply_sn = detail.getString("apply_sn");//备案号
        String state = detail.getString("state");//备案状态
        String remark = detail.getString("remark");//说明
        String productname = detail.getString("productname");//产品名
        String org_name = detail.getString("org_name");//原产地
        String processid=detail.getString("processid");
        String displayname = detail.getString("displayname");
        String provinceConfirm = detail.getString("provinceConfirm");//备案日期

        String enterprise_address = scqyUnitinfo.getString("enterprise_address");//企业地址
        String enterprise_name = scqyUnitinfo.getString("enterprise_name");//企业名称
        //生产厂家，现要求只取一家实际生产企业***
        JSONObject firstSjscqy = sjscqyList.getJSONObject(0);
        String sjscqyName="";
        String sjscqyAddress="";
        String sjscqyHelthPermits="";

        if (firstSjscqy!=null){
            sjscqyName=firstSjscqy.getString("enterprise_name");
            sjscqyAddress=firstSjscqy.getString("enterprise_address");
            sjscqyHelthPermits=firstSjscqy.getString("enterprise_healthpermits");
        }

//        JSONArray sjscqys = new JSONArray();
//        for (int i=0;i<sjscqyList.length();i++){
//            JSONObject scqy = sjscqyList.getJSONObject(i);
//            JSONObject scqyInfo = new JSONObject();
//            scqyInfo.put("企业名称",scqy.getString("enterprise_name"));
//            scqyInfo.put("企业地址",scqy.getString("enterprise_address"));
//            scqyInfo.put("卫生许可证号",scqy.getString("enterprise_healthpermits"));
//            sjscqys.put(scqyInfo);
//        }
        //成分
        List<String> chengFens = new ArrayList<String>();
        for (int i=0;i<pfList.length();i++){
            JSONObject jo = pfList.getJSONObject(i);
            String chengFen = jo.getString("cname");
            chengFens.add(chengFen);
        }
        GCFTProduct gcftProduct = new GCFTProduct();
        gcftProduct.setName(productname);
        gcftProduct.setOrgName(org_name);
        gcftProduct.setApplySN(apply_sn);
        gcftProduct.setApplyState(state);
        gcftProduct.setRemark(remark);
        gcftProduct.setDisplayName(displayname);
        gcftProduct.setProvinceConfirm(provinceConfirm);
        gcftProduct.setEnterpriseAddress(enterprise_address);
        gcftProduct.setEnterpriseName(enterprise_name);
        //gcftProduct.setSjscqys(sjscqys.toString());
        gcftProduct.setSjscqyName(sjscqyName);
        gcftProduct.setSjscqyAddress(sjscqyAddress);
        gcftProduct.setSjscqyHealth(sjscqyHelthPermits);
        gcftProduct.setInfoMations(chengFens);
        return gcftProduct;
    }

}
