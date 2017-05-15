package com.example.scrapy;

import com.example.entity.AmazonProduct;
import com.example.entity.GCFTProduct;
import com.example.result.Result;
import com.example.util.*;
import com.example.util.UnirestUtil;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mqm on 2017/5/11.
 */
@Component
public class GcftScrapy {

    private final static Logger logger = LoggerFactory.getLogger(GcftScrapy.class);

    int gcftLine = 0;

    public void Scrapy(Integer fromPage,Integer toPage,String ku) throws Exception {
        if (ku.equals("gcft")){//国产非特殊用途库
            for(int i=fromPage;i<=toPage;i++){
                gcft(i);
                logger.info("国产非特殊用途，正在爬取，第"+ i+"页");
            }
            logger.info("******国产非特殊用途爬取完成**********");
        }
    }

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
        //生产厂家
        JSONArray sjscqys = new JSONArray();
        for (int i=0;i<sjscqyList.length();i++){
            JSONObject scqy = sjscqyList.getJSONObject(i);
            JSONObject scqyInfo = new JSONObject();
            scqyInfo.put("企业名称",scqy.getString("enterprise_name"));
            scqyInfo.put("企业地址",scqy.getString("enterprise_address"));
            scqyInfo.put("卫生许可证号",scqy.getString("enterprise_healthpermits"));
            sjscqys.put(scqyInfo);
        }
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
        gcftProduct.setSjscqys(sjscqys.toString());
        gcftProduct.setInfoMations(chengFens);
        return gcftProduct;
    }

}
