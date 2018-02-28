package com.water.ce.web.service.impl;

import com.water.ce.http.HttpRequestTool;
import com.water.ce.web.service.FanyiService;
import com.water.uubook.dao.TbCeFetchUrlMapper;
import com.water.uubook.model.TbCeFetchUrl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangmiaojie on 2018/1/8.
 */
@Service("fanyiServiceImpl")
public class FanyiServiceImpl extends CrawlingArticleServiceImpl implements FanyiService {

    @Resource
    private TbCeFetchUrlMapper fetchUrlMapper;

    @Override
    public void handle() {
        this.fetchWebsite01();
    }

    public void fetchWebsite01() {
        String html;
        Document doc;
        String fetchTagUrl = "https://jaxenter.com/tag/%s";
        String[] tags = {"DevOps", "Agile", "Java", "Careers", "Open Source", "IoT", "NetBeans", "Eclipse", "JavaScript", "Tutorials"};

        int origin = 100;
        List<TbCeFetchUrl> fetchUrlList = new ArrayList<>();
        Date currentTime = new Date();
        //遍历网站的所有标签
        for (int i = 0; i < tags.length; i++) {
            String requestUrl = String.format(fetchTagUrl, tags[i]);
            for (int page = 1; ; page++) {
                String tempRequestUrl = requestUrl + String.format("/page/%s/", page);
                html = (String) HttpRequestTool.getRequest(tempRequestUrl);
                doc = Jsoup.parse(html);

                Elements linkEles = doc.select(".title > a");
                if (linkEles.size() < 0) break;
                for (Element linkEle : linkEles) {
                    String articleLink = linkEle.attr("href");
                    System.out.println(articleLink);
                    this.addFetchUrlList(fetchUrlList, articleLink, origin, currentTime);
                }
                if (fetchUrlList.size() > 1000) {
                    fetchUrlMapper.insertBatch(fetchUrlList);
                    fetchUrlList.clear();
                }
            }

            fetchUrlMapper.insertBatch(fetchUrlList);
        }

    }
}
