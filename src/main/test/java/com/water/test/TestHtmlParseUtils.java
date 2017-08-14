package com.water.test;

import com.water.crawl.utils.html.HtmlParseUtils;
import com.water.crawl.utils.http.HttpRequestTool;

import java.io.IOException;
import java.util.List;

/**
 * Created by mrwater on 2017/3/20.
 */
public class TestHtmlParseUtils {
    public static void main(String[] args) throws IOException {
        String url = "http://blog.csdn.net/chenaini119/article/details/51850526";
        String result = (String) HttpRequestTool.getRequest(url, false);
        List<String> linkList = HtmlParseUtils.getImgSrc(result);
        for (String link : linkList) {
            System.out.println(link);
        }
    }
}
