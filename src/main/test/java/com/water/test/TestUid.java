package com.water.test;

import com.water.crawl.utils.HttpRequestTool;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhangmiaojie on 2017/2/15.
 */
public class TestUid {

    public static void main(String[] args) {
//        System.out.println(randomUid());
        fetchCourseTopics();
    }

    /**
     * 随机生成10位的用户ID
     *
     * @return
     */
    public static long randomUid() {
        long max = 9999999999L;
        long min = 1000000000L;
        Random random = new Random();
        long id = random.nextInt((int) max) % (max - min + 1) + min;
        return Math.abs(id);
    }

    public void fetchweightWebsite() throws IOException, DocumentException {
        SAXReader reader = new SAXReader();
        org.dom4j.Document xmlDoc = reader.read(new File("E:\\weightWebsite.xml"));
        org.dom4j.Element rootEle = xmlDoc.getRootElement();

        String url = "http://top.chinaz.com/hangye/index_yiliao_br%s.html";
        String requestUrl = "";
        String htmlPage = "";
        Document doc = null;
        List<String> serverList = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            if (i == 1) {
                requestUrl = String.format(url, "");
            } else {
                requestUrl = String.format(url, "_" + i);
            }
            System.out.println("开始抓取:" + requestUrl);
            htmlPage = (String) HttpRequestTool.getRequest(requestUrl, false);
            if (StringUtils.isNotBlank(htmlPage)) {
                doc = Jsoup.parse(htmlPage);
                Elements itemsEle = doc.select(".listCentent li");
                for (Element itemEle : itemsEle) {
                    String weightUrl = itemEle.select(".RtCData img").attr("src");
//                   System.out.println(weightUrl);
                    weightUrl = weightUrl.substring(weightUrl.lastIndexOf("/") + 1, weightUrl.lastIndexOf("."));
                    int weight = Integer.parseInt(weightUrl);
                    if (weight >= 5) {
                        org.dom4j.Element produceEle = DocumentHelper.createElement("produce");

                        org.dom4j.Element nameEle = DocumentHelper.createElement("name");
                        String produceName = itemEle.select(".rightTxtHead a").text();
                        nameEle.setText(produceName);
                        produceEle.add(nameEle);

                        org.dom4j.Element serverEle = DocumentHelper.createElement("server");
                        String serverName = itemEle.select(".col-gray").text();
                        serverEle.setText(serverName);
                        produceEle.add(serverEle);

                        org.dom4j.Element weightEle = DocumentHelper.createElement("weight");
                        weightEle.setText(weight + "");
                        produceEle.add(weightEle);

                        rootEle.add(produceEle);
                    }
                }
            } else {
                System.out.println("抓取不到数据 -> " + requestUrl);
            }
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(new File("E:\\weightWebsite1.xml")), "utf-8"), format);
        writer.write(xmlDoc);
        writer.close();
    }

    public static void fetchCourseTopics() {
        String requestUrl = "http://www.yiibai.com/";
        String htmlPage = (String) HttpRequestTool.getRequest(requestUrl, false);
        if (htmlPage == null) return;
        Document doc = Jsoup.parse(htmlPage);
        Elements itemElements = doc.select(".item");
        for (Element item : itemElements) {
            String parentNode = item.select("h2").text();
            System.out.println("教程专题：" + parentNode);
            Elements sonElements = item.select(".blogroll a");
            for (Element sonEle : sonElements) {
                String sonNode = sonEle.text();
                String description = sonEle.attr("title");
                String url = sonEle.absUrl("href");
                if (StringUtils.isNotBlank(url)) {
                    System.out.println("开始抓取 -> " + sonNode + " - > " + url);
                    htmlPage = (String) HttpRequestTool.getRequest(url, false);
                    if (htmlPage == null) continue;
                    Document sonDoc = Jsoup.parse(htmlPage);
                    Elements menus = sonDoc.select(".pagemenu li a");
                    for (Element oneItem : menus) {
                        String title = oneItem.text();
                        String articleUrl = oneItem.absUrl("href");
                        System.out.println("标题:" + title + " | 链接=" + articleUrl);
                    }
                }
                System.out.println(sonNode + ":" + description);
            }
            System.out.println("---------------------------------------------------------------------------------");
        }
//        System.out.println(htmlPage);
    }
}
