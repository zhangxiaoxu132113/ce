package com.water.crawl.core;

import com.water.crawl.core.factory.CrawlFactory;
import org.apache.bcel.util.ClassLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrwater on 2017/3/18.
 */
public class CrawlBox {
    private String filePath = "crawl";
    private static Map<String, List<CrawlRule>> crawlContainer = new HashMap<>();
    private Log logger = LogFactory.getLog(getClass());

    public void initialize() {
        String realFilePath = ClassLoader.getSystemResource(filePath).getPath();
        File file = new File(realFilePath);
        if (!file.exists()) {
            throw new RuntimeException("初始化失败！找不到" + filePath);
        }
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String fileName = pathname.getName();
                String[] arg1 = fileName.split("\\.");
                if (arg1.length != 2) {
                    logger.warn("文件名" + fileName + "格式不正确，无法加载该文件！");
                    return false;
                }
                String name = arg1[0];
                String extendName = arg1[1];
                if (!"json".equals(extendName)) {
                    return false;
                }
                return true;
            }
        });
        if (files.length > 0) {
            for (File jsonFile : files) {
                loadCrawl(jsonFile);
            }
        }

    }

    public void loadCrawl(String filePath) {
        List<CrawlRule> crawlRules = CrawlFactory.getCrawlRulelist(filePath);
        put(crawlRules);
    }

    public void loadCrawl(File filePath) {
        List<CrawlRule> crawlRules = CrawlFactory.getCrawlRulelist(filePath);
        put(crawlRules);
    }

    private void put(List<CrawlRule> crawlRules) {
        for (CrawlRule rule : crawlRules) {
            for (CrawlRuleArray ruleArray : rule.getModule()) {
                crawlContainer.put(rule.getId() + "_" + ruleArray.getMid(), ruleArray.getRuleList());
            }
        }
    }

    public List<CrawlRule> getValue(String key) {
        List<CrawlRule> ruleList = crawlContainer.get(key);
        if (ruleList == null) {
            throw new RuntimeException("爬虫不存在" + key);
        }
        return ruleList;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static void main(String[] args) {
//        CrawlBox crawlBox = CrawlBox.getInstance();
//        crawlBox.initialize();

    }

    public static String getCrawlKey(String id, String module) {
        return id + "_" + module;
    }
}
