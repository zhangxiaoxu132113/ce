package com.water.ce.work.fetchTag;

import com.water.ce.cache.CacheManager;
import com.water.ce.utils.http.HttpRequestTool;
import com.water.uubook.dao.TbUbBaseMapper;
import com.water.uubook.dao.TbUbCategoryMapper;
import com.water.uubook.dao.TbUbTagMapper;
import com.water.uubook.model.TbUbBase;
import com.water.uubook.model.TbUbCategory;
import com.water.uubook.model.TbUbTag;
import com.water.uubook.model.dto.TbUbBaseDto;
import com.water.uubook.model.dto.TbUbCategoryDto;
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
 * Created by zhangmiaojie on 2017/8/15.
 */
@Service
public class FetchTagTask {
    private String url;
    private final static String QUEUE_TAG = "queue_tag_%s";

    @Resource
    private CacheManager cacheManager;
    @Resource
    private TbUbTagMapper tagMapper;
    @Resource
    private TbUbBaseMapper baseMapper;
    @Resource
    private TbUbCategoryMapper categoryMapper;
    private Log logger = LogFactory.getLog(FetchTagTask.class);

    public FetchTagTask() {
    }

    public FetchTagTask(String url1) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//    @Override
    public void run() {
        System.out.println(getUrl());
        System.out.println(Thread.currentThread().getName());
        if (StringUtils.isBlank(getUrl())) {
            throw new RuntimeException("传入的根连接不能为空！");
        }
        if (getUrl().contains("/article/details")) {
            String content = (String) HttpRequestTool.getRequest(getUrl());
            Document doc = Jsoup.parse(content);
            String category = "";
            Elements categoeyEles = doc.select(".category_r > label > span");
            if (categoeyEles != null && categoeyEles.size() == 1) {
                category = categoeyEles.get(0).ownText();
                logger.info(category);
            }
            if (StringUtils.isNotBlank(category)) {
                String queueName = String.format(QUEUE_TAG, category);
                Elements tagEles = doc.select(".link_categories > a");
                if (tagEles != null && tagEles.size() > 0) {
                    for (Element tagEle : tagEles) {
                        String tag = tagEle.ownText();
                        cacheManager.sadd(queueName, tag);
                        logger.info(tag);
                    }
                }
            }
        }
    }

    public void handleTag() {
        TbUbTag tag;
        List<TbUbTag> tagList = new ArrayList<>();
        List<TbUbCategory> categories = categoryMapper.selectByExample(null);
        for (TbUbCategory category : categories) {
            tag = new TbUbTag();
            tag.setName(category.getName());
            tag.setCategoryId(category.getId());
            tag.setWeight(10f);
            tag.setParentId(0);
            tag.setEnable(false);
            tagList.add(tag);
        }
        tagMapper.insertBatch(tagList);
        tagList.clear();
        List<TbUbBaseDto> baseList = baseMapper.getAllTbUbBase();
        baseList = this.getTreeBaseList(baseList);
        baseList.stream().forEach(p -> {
            int categoryId = p.getCategoryId();
            recursion(p.getChildren(), tagList, categoryId, categoryId);
        });
    }

    private void recursion(List<TbUbBaseDto> baseDtoList, List<TbUbTag> tagList, int parentId, int categoryId) {
        if (baseDtoList != null) {
            for (TbUbBaseDto tbUbBaseDto : baseDtoList) {
                float weight = 0;
                TbUbTag tag = new TbUbTag();
                tag.setName(tbUbBaseDto.getName());
                tag.setCategoryId(categoryId);
                tag.setParentId(parentId);
                tag.setCreateTime(new Date());
                tag.setEnable(false);
                if (tbUbBaseDto.getLevel() == 1) {
                    weight = 8;
                } else if (tbUbBaseDto.getLevel() == 2) {
                    weight = 7;
                } else if (tbUbBaseDto.getLevel() == 3) {
                    weight = 6;
                }
                tag.setWeight(weight);
                int tagId = tagMapper.insert(tag);

                tagList.add(tag);
                List<TbUbBaseDto> baseDtos = tbUbBaseDto.getChildren();
                if (baseDtos == null || baseDtos.size() == 0) {
                    continue;
                }
                recursion(baseDtos, tagList, tagId, categoryId);
            }
        }
    }

    private List<TbUbBaseDto> getTreeBaseList(List<TbUbBaseDto> baseList) {
        List<TbUbBaseDto> treeBaseList = new ArrayList<>();
        for (TbUbBaseDto a : baseList) {
            for (TbUbBaseDto b : baseList) {
                if (b.getParentId().equals(a.getId())) {
                    if (a.getChildren() == null) {
                        a.setChildren(new ArrayList<>());
                    }
                    a.getChildren().add(b);
                    b.setParent(a);
                }
            }
            if (a.getLevel() == 0) {
                treeBaseList.add(a);
            }
        }
        return treeBaseList;
    }
}
