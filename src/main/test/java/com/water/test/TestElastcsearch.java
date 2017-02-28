package com.water.test;

import com.alibaba.fastjson.JSONObject;
import com.water.crawl.db.model.ITArticle;
import com.water.crawl.utils.ElasticSearchUtils;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by zhangmiaojie on 2017/2/28.
 */
public class TestElastcsearch {

    public static void main(String[] args) {
        ElasticSearchUtils.searchDocumentByTerm("mysql");
    }
}
