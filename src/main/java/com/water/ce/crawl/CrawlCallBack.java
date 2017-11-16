package com.water.ce.crawl;

import com.google.gson.JsonObject;
import com.water.ce.utils.http.HttpRequestTool;
import org.apache.commons.lang3.StringUtils;
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
        if (StringUtils.isNoneBlank(result)) {
            Document doc = Jsoup.parse(result);
            JsonObject jsonObject = parseHtml2Json(doc, crawlRuleList);
            if (jsonObject != null) {
                calBack(jsonObject, url);
            }
        }
    }

    /**
     * 根据json配置文件解析dom
     * @param doc           html内容
     * @param crawlRules    解析规则
     * @return              JsonObject
     */
    public JsonObject parseHtml2Json(Document doc, List<CrawlRule> crawlRules) {
        int retryCount = 0;
        Elements elements = null;
        JsonObject jsonObject = new JsonObject();
        for (CrawlRule rule : crawlRules) {
            String id = rule.getId();
            String value = "";
            String ruleStr;
            String ruleStrs[] = rule.getRule();
            while (true) {
                if (retryCount > ruleStrs.length - 1) break; //解析该字段失败，返回空字符串
                ruleStr = ruleStrs[retryCount];
                elements = doc.select(ruleStr);
                if (elements.size() > 0) {
                    value = getValue(elements.get(0), rule.getGet());
                    break;
                }
                retryCount++;
            }

            jsonObject.addProperty(id, value);
        }
        return jsonObject;
    }

    public String getValue(Element ele, String type) {
        if (StringUtils.isBlank(type)) {
            throw new RuntimeException("获取的类型不合法！");
        }
        String value = "";
        if (type.equals("text")) {
            value = ele.text();
        } else if (type.equals("ownText")) {
            value = ele.ownText();
        } else if (type.equals("html")) {
            value = ele.html();
        } else if (type.equals("attr:href")) {
            value = ele.attr("href");
        } else if (type.equals("attr:content")) {
            value = ele.attr("content");
        }
        return value;
    }

    public abstract void calBack(JsonObject object, String url);
}
