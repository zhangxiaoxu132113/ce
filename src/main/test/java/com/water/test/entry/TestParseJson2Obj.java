package com.water.test.entry;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mrwater on 2017/3/18.
 */
public class TestParseJson2Obj {
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
//        List<CrawlRule> crawlRuleList = null;
//        InputStream is = ClassLoader.getSystemResourceAsStream("");
//        String json = inputStream2String(is);
//        Type listType = new TypeToken<ArrayList<CrawlRule>>() {}.getType();
//        crawlRuleList = gson.fromJson(json, listType);
//        System.out.println(crawlRuleList.get(0).getId());
//        CrawlAction("IBM","article", "").work();
//        CrawlAction crawlAction = new CrawlAction<Article>("ibm","article") {
//            @Override
//            public void action(Article cls) {
//
//            }
//        };
//        crawlAction.work();
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("title","a little love");
//        jsonObject.addProperty("author","mr chen");
//        jsonObject.addProperty("content", "i love you");
//        System.out.println(jsonObject.toString());
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
