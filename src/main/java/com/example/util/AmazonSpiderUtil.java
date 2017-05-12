package com.example.util;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 陈成 on 2017/5/12.
 */
@Component
public class AmazonSpiderUtil {

    public String getXmlByHtmlunit(String url) throws Exception {

        String ret = "";
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setTimeout(10000);
        WebRequest webRequest = new WebRequest(new URL(url));
        webRequest.setHttpMethod(HttpMethod.GET);
        HtmlPage page = webClient.getPage(webRequest);
        //webClient.waitForBackgroundJavaScript(1000);
        ret = page.asXml();
        webClient.closeAllWindows();
        return ret;
    }

    public Document getDocument (String url ) throws Exception{
        return Jsoup.connect(url).timeout(60000)
                .get();

    }
    public String getURLSource(String  str) throws Exception    {
        URL url = new URL(str);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        //conn.setConnectTimeout(5 * 1000);
        InputStream inStream =  conn.getInputStream();
        byte[] data = readInputStream(inStream);
        String htmlSource = new String(data);
        return htmlSource;
    }


    public  byte[] readInputStream(InputStream instream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[]  buffer = new byte[1204];
        int len = 0;
        while ((len = instream.read(buffer)) != -1){
            outStream.write(buffer,0,len);
        }
        instream.close();
        return outStream.toByteArray();
    }
}
