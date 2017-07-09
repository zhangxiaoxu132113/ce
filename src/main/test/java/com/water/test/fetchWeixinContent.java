package com.water.test;

import com.water.crawl.utils.http.HttpRequestTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangmiaojie on 2017/6/6.
 */
public class fetchWeixinContent {

    /**
     * 抓取丁香医生的标题数据
     * @param args
     */
    public static void main(String[] args) {
        String url = "http://www.anyv.net/index.php/account-44371";
        List<String> titleList = new ArrayList<>();
        String body;
        Document doc;
        body = (String) HttpRequestTool.getRequest(url);
        doc = Jsoup.parse(body);
        Elements elements = doc.select(".group  h3  a");
        for (Element ele : elements) {
            titleList.add(ele.text());
        }

        url += "-page-%s";
        for (int i=2; i<=227;i++) {
            System.out.println("第"+i+"篇");
            String tmpUrl = url;
            tmpUrl = String.format(tmpUrl,i);
            body = (String) HttpRequestTool.getRequest(tmpUrl);
            doc = Jsoup.parse(body);
            elements = doc.select(".group  h3  a");
            for (Element ele : elements) {
                titleList.add(ele.text());
            }
        }

        System.out.println("\n\n\n");
        System.out.println("一共抓取"+titleList.size()+"篇文章");
        for (String title : titleList) {
            System.out.println(title);
        }
    }
}
