package com.water.crawl.db.service.article.impl;

import com.alibaba.fastjson.JSONObject;
import com.water.crawl.db.dao.ITLibMapper;
import com.water.crawl.db.model.ITLib;
import com.water.crawl.db.service.article.ICSDNArticleService;
import com.water.crawl.utils.Constant;
import com.water.crawl.utils.HttpRequestTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by zhangmiaojie on 2017/3/1.
 */
@Service("csdnArticleService")
public class CSDNArticleServiceImpl extends ITArticleServiceImpl implements ICSDNArticleService {

    private static String CSDN_LIB_URL = "http://lib.csdn.net/bases";
    @Resource
    private ITLibMapper itLibMapper;

    @Override
    public Integer addAllLibCategory() {
        ITLib itLib = null;
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

                queryParams.put("name",name);
                List<ITLib> itLibList = this.queryLibByCondition(queryParams);
                if (itLibList.size()==0) {
                    itLib = new ITLib();
                    itLib.setId(UUID.randomUUID().toString());
                    itLib.setName(name);
                    itLib.setUrl(link);
                    itLib.setPic(pic);
                    itLib.setBgPic(bgPic);
                    itLib.setCategory(Constant.ArticleCategory.CSDN.getIndex());
                    itLib.setCreateOn(System.currentTimeMillis());
                    effectCount += itLibMapper.insert(itLib);
                }
            }
        }
        return effectCount;
    }

    @Override
    public List<ITLib> getAllLibCategory() {
        return itLibMapper.getAllLib();
    }

}
