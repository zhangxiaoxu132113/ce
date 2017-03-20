package com.water.crawl.core.factory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.water.crawl.core.CrawlRule;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrwater on 2017/3/18.
 */
public class CrawlFactory {
    private static final Gson gson = new Gson();

    public static List<CrawlRule> getCrawlRulelist(String filePath) {
        return getCrawlRulelist(getInputStream(filePath));
    }

    public static List<CrawlRule> getCrawlRulelist(File filePath) {
        return getCrawlRulelist(getInputStream(filePath));
    }

    private static List<CrawlRule> getCrawlRulelist(InputStream inputStream){
        return getRealCrawlRulelist(inputStream);
    }

    private static List<CrawlRule> getRealCrawlRulelist(InputStream inputStream) {
        List<CrawlRule> crawlRuleList = null;
        try {
            String json = inputStream2String(inputStream);
            Type listType = new TypeToken<ArrayList<CrawlRule>>() {}.getType();
            crawlRuleList = gson.fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return crawlRuleList;
    }

    private static InputStream getInputStream(String filePath) {
        return ClassLoader.getSystemResourceAsStream(filePath);
    }

    private static InputStream getInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String inputStream2String(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line ;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
