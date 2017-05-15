package com.example.scrapy;

import com.example.result.Result;
import com.example.util.HttpClientUtil;
import com.example.util.UnirestUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
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

/**
 * Created by mqm on 2017/5/11.
 */
@Component
public class GcftScrapy {

    private final static Logger logger = LoggerFactory.getLogger(GcftScrapy.class);

    int gcftLine = 0;

    public void Scrapy(Integer fromPage,Integer toPage,String ku) throws Exception {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        HttpSession session = request.getSession();

        if (ku.equals("gcft")){//国产非特殊用途库
            for(int i=fromPage;i<=toPage;i++){
                gcft(i);
                logger.info("正在爬取，第"+ i+"页");
            }
        }
    }

    public void gcft(Integer pageIndex) throws Exception {
        String url = "http://125.35.6.80:8080/ftba/itownet/fwAction.do?method=getBaNewInfoPage";
        JSONObject paramsJSON = new JSONObject();
        paramsJSON.put("page", 1);
        paramsJSON.put("pageSize", 15);
        Result result = UnirestUtil.ajaxPost(url,paramsJSON);
        if (result.getCode()==200 && result.getMessage().equals("OK")){
            JSONObject jsonObject = (JSONObject) result.getData();
            JSONArray productList = jsonObject.getJSONArray("list");
            for (int i = 0;i<productList.length();i++){
                JSONObject product = productList.getJSONObject(i);
                String processid = product.getString("processid");

                //获取详细信息的请求链接
                String detailUrl = "http://125.35.6.80:8080/ftba/itownet/fwAction.do?method=getBaInfo";
                String detailParams = "processid="+processid;
                Result detailResult = UnirestUtil.ajaxPost(detailUrl,detailParams);
                if (detailResult.getCode()==200 && detailResult.getMessage().equals("OK")){
                    JSONObject detail = (JSONObject) detailResult.getData();
                    dealGcftDetail(detail);
                }
            }

        }
    }

    public void dealGcftDetail(JSONObject detail) throws IOException {
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
        String provinceConfirm = detail.getString("provinceConfirm");//备案市日期

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

        Workbook wb = new XSSFWorkbook();
        Sheet sheet1 = wb.createSheet("gcft");
        FileOutputStream fileOut = new FileOutputStream("gcft.xlsx");


        wb.write(fileOut);
        fileOut.close();
    }

}
