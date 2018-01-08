package com.water.ce.web.service.impl;

import com.water.ce.http.HttpRequestTool;
import com.water.ce.utils.QueueClientHelper;
import com.water.ce.utils.lang.StringUtil;
import com.water.ce.web.model.dto.CrawlerArticleUrl;
import com.water.ce.web.service.InfoQService;
import com.water.uubook.dao.TbCeFetchUrlMapper;
import com.water.uubook.model.TbCeFetchUrl;
import com.water.uubook.utils.Constants;
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
 * Created by mrwater on 2017/12/9.
 */
@Service("InfoQServiceImpl")
public class InfoQServiceImpl extends CrawlingArticleServiceImpl implements InfoQService {

    private final static String SOFTWARE_BASE_URL = "http://www.infoq.com";
    private final static String WEB_SITE = "www.infoq.com";
    private final static String MODULE = "toutiao";


    private static Log log = LogFactory.getLog(InfoQServiceImpl.class);

    @Resource
    private TbCeFetchUrlMapper fetchUrlMapper;

    @Override
    public void handle() {

    }

    public void fetchToutiao() {
        String url = "http://www.infoq.com/cn/news/%s";
        long startTime = System.currentTimeMillis();
        log.info("【InfoQ】爬虫任务====================>开始");

        CrawlerArticleUrl crawlerArticleUrl;
        List<ProtoEntity> crawlerArticleUrlList = new ArrayList<>();
        TbCeFetchUrl fetchUrl;
        List<TbCeFetchUrl> fetchUrlList = new ArrayList<>();

        String html;
        String requestUrl;
        Document doc;
        int currentPage = 1;
        Date currentTime = new Date();
        for (int i = 0; ; i += 15) {
            System.out.println("当前第" + currentPage + "页");
            if (i == 0) {
                requestUrl = String.format(url, "");
            } else {
                requestUrl = String.format(url, i);
            }
            html = (String) HttpRequestTool.getRequest(requestUrl);
            if (StringUtils.isBlank(html)) {
                i -= 15;
                continue;
            }
            doc = Jsoup.parse(html);
            Elements linkEles = doc.select("#content .news_type_block > h2 a");
            if (linkEles == null || linkEles.size() <= 0) {
                break;
            }
            for (Element linkEle : linkEles) {
                String link = SOFTWARE_BASE_URL + linkEle.attr("href");
                crawlerArticleUrl = new CrawlerArticleUrl();
                crawlerArticleUrl.setUrl(link);
                crawlerArticleUrl.setWebSite(WEB_SITE);
                crawlerArticleUrl.setWebSiteModule(MODULE);
                crawlerArticleUrl.setModule(Constants.ARTICLE_MODULE.TOU_TIAO);
                crawlerArticleUrlList.add(crawlerArticleUrl);

                fetchUrl = new TbCeFetchUrl();
                fetchUrl.setUrl(link);
                fetchUrl.setOrigin(3);
                fetchUrl.setCreateOn(currentTime);
                fetchUrlList.add(fetchUrl);
            }
            if (fetchUrlList.size() > 1000) {
                fetchUrlMapper.insertBatch(fetchUrlList);
                fetchUrlList.clear();
            }
            currentPage++;
        }
        fetchUrlMapper.insertBatch(fetchUrlList);
        String taskId = StringUtil.uuid();
        recordTask(taskId, "【InfoQ】爬虫任务", crawlerArticleUrlList.size());
        submitCrawlingTask(WEB_SITE, MODULE, taskId, crawlerArticleUrlList, QueueClientHelper.FETCH_ARTICLE_QUEUE);
        log.info("爬虫任务" + taskId + "已经提交到爬虫队列");
        long endTime = System.currentTimeMillis();
        log.info("【InfoQ】爬虫任务执行结束，一共耗时为" + (endTime - startTime));
    }
}
