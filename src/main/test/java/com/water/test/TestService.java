package com.water.test;

import com.water.crawl.utils.http.HttpRequestTool;
import com.water.crawl.utils.lang.StringUtil;
import com.water.crawl.work.FetchArticleTask;
import org.apache.catalina.core.ApplicationContext;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangmiaojie on 2017/4/13.
 */
public class TestService {

    public static void main(String[] args) throws UnsupportedEncodingException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
        FetchArticleTask fetchArticleTask = (FetchArticleTask) context.getBean("fetchArticleTask");
        fetchArticleTask.fetchIBMArticles();
    }

    public static String getUTF8StringFromGBKString(String gbkStr) {
        try {
            return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InternalError();
        }
    }

    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }
}
