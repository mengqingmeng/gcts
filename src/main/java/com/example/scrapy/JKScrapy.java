package com.example.scrapy;

import com.example.entity.JKProduct;
import com.example.result.Result;
import com.example.util.UnirestUtil;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mqm on 2017/5/23.
 */
@Component
public class JKScrapy {
    private final static Logger logger = LoggerFactory.getLogger(JKScrapy.class);

    public void jk(Integer pageIndex) throws Exception {
        String url = "http://app1.sfda.gov.cn/datasearch/face3/search.jsp";
        Result result = UnirestUtil.ajaxPostStr(url,"tableId=69&State=1&tableName=TABLE69&curstart="+pageIndex);
        if (result.getCode()==200 && result.getMessage().equals("OK")){
            String data = (String) result.getData();
            Document doc = Jsoup.parse(data);
            Elements as = doc.getElementsByTag("a");
            List<JKProduct> products = new ArrayList<JKProduct>();
        }
    }

    /**
     * 获取验证码
     * @return
     * @throws UnirestException
     * @throws IOException
     */
    public Result<String> getVeriCode() throws UnirestException, IOException {
        String urlPath = "http://123.127.80.6/servlet/GetImageServlet?sn=randomImage";
        Connection conn = Jsoup.connect(urlPath).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36");
        Connection.Response response = conn.execute();
        byte[] data = response.bodyAsBytes();
        InputStream is = new ByteArrayInputStream(data);
        BufferedImage grayImage = ImageHelper.convertImageToBinary(ImageIO.read(is));
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        Result<String> result = new Result<String>();
        try {
            result.setCode(1);
            result.setData(instance.doOCR(grayImage));
            result.setMessage("success");
        } catch (TesseractException e) {
            result.setCode(0);
            result.setData(e.getLocalizedMessage());
            result.setMessage("error");
        }
        return result;
    }

}
