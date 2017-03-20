package com.water.crawl.core;

import com.google.gson.JsonObject;
import com.water.crawl.utils.HttpRequestTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by mrwater on 2017/3/19.
 */
public abstract class CrawlCallBack implements Runnable {
    private String crawlKey;
    private String url;

//    @Resource(name = "crawlBox")
    private CrawlBox crawlBox = new CrawlBox();

    public CrawlCallBack(String crawlKey, String url) {
        this.crawlKey = crawlKey;
        this.url = url;
    }

    @Override
    public void run() {
        List<CrawlRule> crawlRuleList = crawlBox.getValue(crawlKey);
        if (crawlRuleList == null) {
            throw new RuntimeException(crawlKey + "爬虫不存在！");
        }
        String result = (String) HttpRequestTool.getRequest(url, false);
        Document doc = Jsoup.parse(result);
        JsonObject jsonObject = parseHtml2Json(doc, crawlRuleList);
        if (jsonObject != null) {
            calBack(jsonObject);
        }
    }

    public JsonObject parseHtml2Json(Document doc, List<CrawlRule> crawlRules) {
        int retryCount = 0;
        Elements elements = null;
        JsonObject jsonObject = new JsonObject();
        for (CrawlRule rule : crawlRules) {
            String id = rule.getId();
            String value;
            String ruleStr;
            String ruleStrs[] = rule.getRule();
            while (true) {
                ruleStr = ruleStrs[retryCount];
                elements = doc.select(ruleStr);
                if (elements.size() > 0) {
                    value = getValue(elements.get(0), rule.getGet());
                    break;
                }
                retryCount++;
                if (retryCount > ruleStr.length()) return null;
            }

            jsonObject.addProperty(id, value);
        }
        return jsonObject;
    }

    public String getValue(Element ele, String type) {
        String value = "";
        if (type.equals("text")) {
            value = ele.text();
        } else if (type.equals("ownText")) {
            value = ele.ownText();
        } else if (type.equals("html")) {
            value = ele.html();
        } else if (type.equals("href")) {
            value = ele.attr("href");
        }
        return value;
    }

    public abstract void calBack(JsonObject object);
}
