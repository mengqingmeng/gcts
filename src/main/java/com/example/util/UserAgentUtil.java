package com.example.util;

import java.util.Random;

/**
 * Created by mqm on 2017/6/2.
 */
public class UserAgentUtil {

    public static String[] agents = {"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1"
            ,"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50"
    ,"Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11"
    ,"Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11"
            ,"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56"
            ,"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)",
    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SE 2.X MetaSr 1.0; SE 2.X MetaSr 1.0; .NET CLR 2.0.50727; SE 2.X MetaSr 1.0)"};

    public static String getRandomAgent(){
        int agentsLength = agents.length;
        Random random = new Random();
        int index = random.nextInt(agentsLength)+1;
        return agents[index];
    }

}
