package com.water.ce.utils.html;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrwater on 2017/3/20.
 */
public class HtmlParseUtils {

    /**
     * 获取页面的a标签的href属性对应的值
     * @param htmlBody
     * @return
     */
    public static List<String> getHrefWithA(String htmlBody) {
        Document doc = Jsoup.parse(htmlBody);
        return getHrefWithA(doc);
    }

    public static List<String> getHrefWithA(Document doc) {
        List<String> hrefLists = new ArrayList<String>();
        Elements linkEles = doc.select("a");
        String link;
        for (Element linkEle : linkEles) {
            link = linkEle.absUrl("href");
            //TODO BUG 不能获取绝对路径
            if (StringUtils.isNotBlank(link)) {
                hrefLists.add(link);
            }
        }
        return hrefLists;
    }


    /**
     * 获取网页标题
     * @param page
     * @return
     */
    public static String getPageTitle(String page) {
        if (StringUtils.isBlank(page)) {
            throw new RuntimeException("内容不能为空！");
        }
        Document doc = Jsoup.parse(page);
        return getPageTitle(doc);
    }

    public static String getPageTitle(Document doc) {
        Element titleEle = doc.select("title").get(0);
        return titleEle.text();
    }

    /**
     * @param page
     * @return
     * @descriptino 获取页面的img标签的src属性对应的值
     */
    public static List<String> getImgSrc(String page) {
        if (StringUtils.isBlank(page)) {
            throw new RuntimeException("内容不能为空！");
        }
        Document doc = Jsoup.parse(page);
        return getImgSrc(doc);
    }

    public static List<String> getImgSrc(Document doc) {
        List<String> imgSrcList = new ArrayList<>();
        Elements imgEles = doc.getElementsByTag("img");
        String imgSrc ;
        for (Element imgEle : imgEles) {
            imgSrc = imgEle.attr("src");
            if (StringUtils.isNoneBlank(imgSrc))
                imgSrcList.add(imgSrc);
        }
        return imgSrcList;
    }



}
