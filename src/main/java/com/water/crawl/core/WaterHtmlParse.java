package com.water.crawl.core;


import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrwater on 16/5/25.
 */
public class WaterHtmlParse {

    /**
     * @param httpBody
     * @return
     * @descriptino 获取页面的img标签的src属性对应的值
     */
    public static List<String> getImgSrc(String httpBody) {
        if (httpBody == null && "".equals(httpBody)) return null;
        List<String> imageSrcList = new ArrayList<String>();
        try {
            Parser parser = new Parser(httpBody);
            NodeFilter nodeFilter = new TagNameFilter("img");
            NodeList nodeImgList = parser.extractAllNodesThatMatch(nodeFilter);
            Node eachNode = null;
            ImageTag imageTag = null;

            //遍历存在的img标签
            if (nodeImgList != null && nodeImgList.size() > 0) {
                for (int i = 0; i < nodeImgList.size(); i++) {
                    eachNode = nodeImgList.elementAt(i);
                    if (eachNode instanceof ImageTag) {
                        imageTag = (ImageTag) eachNode;
                        String imageSrc = imageTag.getAttribute("src");
                        if (imageSrc != null && !imageSrc.equals("")) {
                            imageSrcList.add(imageSrc);
                        }
                    }
                }
            }

            return imageSrcList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getPageTitle(String page) {
        String title = null;
        try {
            Parser parser = new Parser(page);
            NodeFilter nodeFilter = new TagNameFilter("title");
            NodeList nodeList = parser.extractAllNodesThatMatch(nodeFilter);
            Node eachNode;
            TitleTag titleTag;
            if (nodeList != null && nodeList.size() == 1) {
                for (int i = 0; i < nodeList.size(); i++) {
                    eachNode = nodeList.elementAt(i);
                    titleTag = (TitleTag) eachNode;
                    title = titleTag.getTitle();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return title;
    }

    /**
     * @param htmlBody
     * @return
     * @descriptino 获取页面的a标签的href属性对应的值
     */
    public static List<String> getHrefWithA(String htmlBody) {
        if (StringUtils.isBlank(htmlBody)) {
            throw new RuntimeException("不能解析内容为空的字符！");
        }
        List<String> hrefLists = new ArrayList<String>();
        Document doc = Jsoup.parse(htmlBody);
        Elements linkElements = doc.select("a");
        for (Element linkEle : linkElements){
            String url = linkEle.absUrl("href");
            if (StringUtils.isNotBlank(url) && (url.startsWith("http")||url.startsWith("https"))) {
//                System.out.println(url);
                hrefLists.add(url);
            }

        }
        return hrefLists;
    }
}
