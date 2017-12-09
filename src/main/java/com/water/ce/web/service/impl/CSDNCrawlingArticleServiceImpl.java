package com.water.ce.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.water.ce.http.HttpRequestTool;
import com.water.ce.utils.QueueClientHelper;
import com.water.ce.utils.lang.StringUtil;
import com.water.ce.web.model.BaseSubject;
import com.water.ce.web.model.dto.CrawlerArticleUrl;
import com.water.ce.web.service.CSDNCrawlingArticleService;
import com.water.uubook.dao.TbCeFetchUrlMapper;
import com.water.uubook.dao.TbUbBaseMapper;
import com.water.uubook.model.TbCeFetchUrl;
import com.water.uubook.model.TbUbBase;
import com.water.uubook.model.TbUbBaseCriteria;
import com.water.uubook.model.dto.TbUbBaseDto;
import com.xpush.serialization.protobuf.ProtoEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by mrwater on 2017/11/19.
 */
@Service("csdnCrawlingArticleService")
public class CSDNCrawlingArticleServiceImpl extends CrawlingArticleServiceImpl implements CSDNCrawlingArticleService {
    private static String CSDN_LIB_URL = "http://lib.csdn.net/bases";
    private static Log log = LogFactory.getLog(CSDNCrawlingArticleServiceImpl.class);

    private static final String WEB_SITE = "csdn.net";

    private static final String MODULE = "base_knowdeage";

    @Autowired
    private TbUbBaseMapper baseMapper;

    @Resource
    private TbCeFetchUrlMapper fetchUrlMapper;

    @Override
    public void handle() {
        long startTime = System.currentTimeMillis();
        log.info("【CSDB知识库】爬虫任务====================>开始");

        String taskId = StringUtil.uuid();
        CrawlerArticleUrl crawlerArticleUrl;
        List<ProtoEntity> crawlerArticleUrlList = new ArrayList<>();
        TbCeFetchUrl fetchUrl;
        List<TbCeFetchUrl> fetchUrlList = new ArrayList<>();

        String html;
        Document doc;
        Date currentTime = new Date();
        Map<String, Object> queryMap = new HashMap<>();
        List<TbUbBaseDto> baseList = this.getAllBaseCategory();
        List<TbUbBaseDto> treeBaseList = this.getTreeBaseList(baseList);
        for (TbUbBaseDto base : treeBaseList) {
            if (base.getLevel() == 0) {
                log.info("抓取：" + base.getName());
            }
            if (base.getChildren() != null) {
                continue;
            }
            String baseLink = base.getUrl();
            int count = 1;
            for (int i = 1; i <= count; i++) {
                String tmpLink = baseLink;
                tmpLink += "?page=" + i;
                queryMap.put("page", String.valueOf(i));
                html = (String) HttpRequestTool.getRequest(tmpLink, false);
                if (StringUtils.isBlank(html)) {
                    break;
                }
                doc = Jsoup.parse(html);
                Elements elements1 = doc.select(".dynamicollect .csdn-tracking-statistics a");
                if (elements1 == null || elements1.size() == 0) {
                    break;
                }
                if (StringUtils.isNotBlank(doc.select("#totalPage").attr("value2"))) {
                    count = Integer.valueOf(doc.select("#totalPage").attr("value2"));
                }

                for (Element ele : elements1) {
                    String articleLink = ele.attr("href");
//                    crawlerArticleUrl = new CrawlerArticleUrl();
//                    crawlerArticleUrl.setUrl(articleLink);
//                    crawlerArticleUrlList.add(crawlerArticleUrl);

                    fetchUrl = new TbCeFetchUrl();
                    fetchUrl.setUrl(articleLink);
                    fetchUrl.setOrigin(2);
                    fetchUrl.setCreateOn(currentTime);
                    fetchUrlList.add(fetchUrl);
                }
                if (fetchUrlList.size() > 1000) {
                    fetchUrlMapper.insertBatch(fetchUrlList);
                    fetchUrlList.clear();
                }
                log.info("pageSize = " + count + " url = " + tmpLink);
            }
        }
        fetchUrlMapper.insertBatch(fetchUrlList);
        long endTime = System.currentTimeMillis();
        //提交爬虫任务
        recordTask(taskId, "【CSDB知识库】爬虫任务", crawlerArticleUrlList.size());
//        submitCrawlingTask(WEB_SITE, MODULE, taskId, crawlerArticleUrlList, QueueClientHelper.FETCH_ARTICLE_QUEUE);
        log.info("【CSDB知识库】爬虫任务执行结束，一共耗时为" + (endTime - startTime));
    }

    private void addAllBaseCategory() {
        List<TbUbBaseDto> baseList = this.getAllBaseCategory();
        List<TbUbBase> newBaseList = new ArrayList<>();
        for (TbUbBase base : baseList) { //遍历每一个知识点
            System.out.println(base.getName());
            String jsonContent = base.getContent();
            if (StringUtils.isBlank(jsonContent)) {
                continue;
            }
            String levelArr[] = new String[10];
            levelArr[0] = base.getId();
            TbUbBase ubBase;
            List<BaseSubject> baseSubjectList = BaseSubject.BaseSubejctFactory.getBaseSubjectList(jsonContent);
            for (BaseSubject baseSubject : baseSubjectList) {
                if (baseSubject.getIndex() == 0) {
                    continue;
                }
                ubBase = new TbUbBase();
                String baseId = StringUtil.uuid();
                ubBase.setId(baseId);
                ubBase.setName(baseSubject.getProp().getName());
                ubBase.setLevel(baseSubject.getIndex());
                ubBase.setUrl(baseSubject.getHref());
                ubBase.setParentId(levelArr[baseSubject.getIndex() - 1]);
                levelArr[baseSubject.getIndex()] = baseId;

                newBaseList.add(ubBase);
            }
        }
        baseMapper.insertBatch(newBaseList);
    }

    private List<TbUbBaseDto> getTreeBaseList(List<TbUbBaseDto> baseList) {
        List<TbUbBaseDto> treeBaseList = new ArrayList<>();
        for (TbUbBaseDto a : baseList) {
            for (TbUbBaseDto b : baseList) {
                if (b.getParentId() == a.getId()) {
                    if (a.getChildren() == null) {
                        a.setChildren(new ArrayList<>());
                    }
                    a.getChildren().add(b);
                    b.setParent(a);
                }
            }
            treeBaseList.add(a);
        }
        return treeBaseList;
    }

    private Integer addAllLibCategory() {
        int effectCount = 0;
        Map<String, String> params = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();
        params.put("type", "all");
        for (int i = 1; ; i++) {
            params.put("page", String.valueOf(i));
            String html = (String) HttpRequestTool.postRequest(CSDN_LIB_URL, params, false);
            System.out.println(html);
            JSONObject jsonObject = JSONObject.parseObject(html);
            html = (String) jsonObject.get("html");
            Document document = Jsoup.parse(html);
            Elements elements = document.select(".whitebk");
            if (elements == null || elements.size() <= 0) {
                System.out.println("已经获取了所有知识库分类");
                break;
            }
            for (Element element : elements) {
                String name = element.select(".title").get(0).ownText();
                String link = element.select(".title").get(0).attr("href");
                String pic = element.select(".topphoto img").get(0).attr("src");
                String bgPic = element.select(".bannerimg img").get(0).attr("src");

                queryParams.put("name", name);
//                List<ITLib> itLibList = this.queryBaseByCondition(queryParams);
//                if (itLibList.size() == 0) {
//                    itLib = new ITLib();
//                    itLib.setId(UUID.randomUUID().toString());
//                    itLib.setName(name);
//                    itLib.setUrl(link);
//                    itLib.setPic(pic);
//                    itLib.setBgPic(bgPic);
//                    itLib.setCreateOn(System.currentTimeMillis());
//                    effectCount += itLibMapper.insert(itLib);
//                }
            }
        }
        return effectCount;
    }

    private List<TbUbBaseDto> getAllBaseCategory() {
        return baseMapper.getAllTbUbBase();
    }

    private List<TbUbBase> queryBaseByCondition(Map<String, Object> queryParams) {
        if (queryParams == null) {
            throw new RuntimeException("参数不能为null!");
        }
        TbUbBaseCriteria baseCriteria = new TbUbBaseCriteria();
        TbUbBaseCriteria.Criteria criteria = baseCriteria.createCriteria();
        if (queryParams.containsKey("name")) {
            criteria.andNameEqualTo((String) queryParams.get("name"));
        } else if (queryParams.containsKey("category")) {
            criteria.andCategoryEqualTo((Integer) queryParams.get("category"));
        }
        return baseMapper.selectByExample(baseCriteria);
    }
}
