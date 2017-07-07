package com.water.crawl.core;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.water.crawl.core.factory.CrawlFactory;
import org.apache.bcel.util.ClassLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 一个抓取页面的解析容器
 * <p>
 * <p>
 * Created by mrwater on 2017/3/18.
 */
public class CrawlBox {
    private Log logger = LogFactory.getLog(getClass());

    private static String filePath = "crawl";
    private static List<CrawlRule> crawlRuleList = new ArrayList<>();
    private static Map<String, List<CrawlRule>> crawlContainer = new HashMap<>();

    public void initialize() {
        logger.info("initialize the crawler container ...");
        logger.info("loading the configuration file ...");
        String realFilePath = ClassLoader.getSystemResource(filePath).getPath();
        File file = new File(realFilePath);
        if (!file.exists()) {
            throw new RuntimeException("failed to initialize. Can't find" + filePath);
        }
        List<File> files = new ArrayList<>();
        loodLoadFiles(files, file);
        if (files.size() > 0) {
            for (File jsonFile : files) {
                loadCrawl(jsonFile);
            }
        }
        logger.info("successfully loaded configuration file, the crawler parsing container initialization is complete");
    }


    /**
     * To iterate over load file
     * @param fileList
     * @param pathFile
     */
    public void loodLoadFiles(List<File> fileList, File pathFile) {
        if (pathFile.isDirectory()) {
            File[] files = pathFile.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (file.isDirectory()) {
                        loodLoadFiles(fileList, file);
                        return false;
                    }
                    String fileName = file.getName();
                    String extendName = fileName.substring(fileName.lastIndexOf(".")+1);
                    if (!"json".equals(extendName)) {
                        return false;
                    }
                    return true;
                }
            });
            fileList.addAll(Arrays.asList(files));
        }
    }

    public void loadCrawl(String filePath) {
        List<CrawlRule> crawlRules = CrawlFactory.getCrawlRulelist(filePath);
        put(crawlRules);
    }

    public void loadCrawl(File filePath) {
        List<CrawlRule> crawlRules = CrawlFactory.getCrawlRulelist(filePath);
        if (crawlRules != null && !crawlRules.isEmpty()) {
            crawlRuleList.add(crawlRules.get(0));
            put(crawlRules);
        }

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
            throw new RuntimeException("The crawler does not exist" + key);
        }
        return ruleList;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static String getCrawlKey(String id, String module) {
        return id + "_" + module;
    }

    public int getCrawlContainerSize() {
        return crawlContainer.size();
    }

    public Map<String, List<CrawlRule>> getAllCrawler() {
        return crawlContainer;
    }

    public static List<CrawlRule> getCrawlRuleList() {
        return crawlRuleList;
    }

    public static void main(String[] args) {
        CrawlBox crawlBox = new CrawlBox();
        crawlBox.initialize();
    }

}
