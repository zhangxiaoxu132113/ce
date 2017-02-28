package com.water.crawl.core.repositories;

import com.water.crawl.db.model.ITArticle;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by zhangmiaojie on 2017/2/28.
 */
public interface ArticleRepositories extends PagingAndSortingRepository<ITArticle, String> {
}
