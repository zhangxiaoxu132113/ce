package com.water.test;

import com.water.crawl.work.FetchArticleTask;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Created by mrwater on 2017/3/21.
 */
public class TestService extends BaseService {
    @Resource
    private FetchArticleTask fetchArticleTask;

    @Test
    public void addTagLib() {
        fetchArticleTask.fetchCSDNArticleLib();
    }
}

