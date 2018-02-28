package com.water.ce.web.service.impl;

import com.water.ce.http.HttpRequestTool;
import com.water.ce.utils.QueueClientHelper;
import com.water.ce.utils.lang.StringUtil;
import com.water.ce.web.model.dto.CrawlerArticleUrl;
import com.water.ce.web.service.ChengxuyuanArticleService;
import com.water.uubook.dao.TbCeFetchUrlMapper;
import com.water.uubook.model.TbCeFetchUrl;
import com.xpush.serialization.protobuf.ProtoEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * origin -> 22
 * Created by mrwater on 2018/2/23.
 */
@Service("chengxuyuanArticleService")
public class ChengxuyuanArticleServiceImpl extends CrawlingArticleServiceImpl implements ChengxuyuanArticleService {
    private static Log log = LogFactory.getLog(ChengxuyuanArticleServiceImpl.class);

    private static final String WEB_SITE = "www.voidcn.com";

    private static final String MODULE = "tag_article";

    @Resource
    private TbCeFetchUrlMapper fetchUrlMapper;

    @Override
    public void handle() {
        long startTime = System.currentTimeMillis();
        log.info("【程序园】爬虫任务====================>开始");

        String taskId = StringUtil.uuid();
        CrawlerArticleUrl crawlerArticleUrl;
        List<ProtoEntity> crawlerArticleUrlList = new ArrayList<>();
        TbCeFetchUrl fetchUrl;
        List<TbCeFetchUrl> fetchUrlList = new ArrayList<>();

        Date currentTime = new Date();
        String baseUrl = "http://www.voidcn.com/tag";
        String html = (String) HttpRequestTool.getRequest(baseUrl);
        String category_url;
        Document doc = Jsoup.parse(html);
        Elements categoryEls = doc.select(".tag-list__itembody > .tagPopup > a");
        for (int i = 0; i < categoryEls.size(); i++) {
            Element ele = categoryEls.get(i);
            category_url = ele.absUrl("href");
            // category_url += "/list-" + i + ".html";
            for (int j = 1; j <= 1000; j++) {
                String tmpUrl = category_url + "/list-" + j + ".html";
                html = (String) HttpRequestTool.getRequest(tmpUrl);
                if (StringUtils.isBlank(html)) continue;
                doc = Jsoup.parse(html);

                Elements hrefEles = doc.select(".aricle_item_info > .title > a");
                for (Element hrefEle : hrefEles) {
                    System.out.println(hrefEle.text() + "|" + hrefEle.absUrl("href"));
                    fetchUrl = new TbCeFetchUrl();
                    fetchUrl.setUrl(hrefEle.absUrl("href"));
                    fetchUrl.setOrigin(22);
                    fetchUrl.setCreateOn(currentTime);
                    fetchUrlList.add(fetchUrl);
                }
                if (fetchUrlList.size() > 1000) {
                    this.fetchUrlMapper.insertBatch(fetchUrlList);
                    fetchUrlList.clear();
                    log.info("insert success!-------------------------------");
                }
            }
            System.out.println(ele.absUrl("href") + ele.text());
        }

        fetchUrlMapper.insertBatch(fetchUrlList);
        long endTime = System.currentTimeMillis();
        //提交爬虫任务
        recordTask(taskId, "【程序园】爬虫任务", crawlerArticleUrlList.size());
        submitCrawlingTask(WEB_SITE, MODULE, taskId, crawlerArticleUrlList, QueueClientHelper.FETCH_ARTICLE_QUEUE);
        log.info("【程序园】爬虫任务执行结束，一共耗时为" + (endTime - startTime));
    }

    public static void main(String[] args) {
        String baseUrl = "http://www.voidcn.com/tag";
        String html = (String) HttpRequestTool.getRequest(baseUrl);
        String category_url;
        Document doc = Jsoup.parse(html);
        Elements categoryEls = doc.select(".tag-list__itembody > .tagPopup > a");
        for (int i = 0; i < categoryEls.size(); i++) {
            Element ele = categoryEls.get(i);
            category_url = ele.absUrl("href");
            category_url += "/list-%s.html";
            for (int j = 1; j <= 1000; j++) {
                String tmpUrl = String.format(category_url, j);
                html = (String) HttpRequestTool.getRequest(tmpUrl);
                doc = Jsoup.parse(html);

                Elements hrefEles = doc.select(".aricle_item_info > .title > a");
                for (Element hrefEle : hrefEles) {
                    System.out.println(hrefEle.text() + "|" + hrefEle.absUrl("href"));
                }
            }
            System.out.println(ele.absUrl("href") + ele.text());
        }
    }
}
