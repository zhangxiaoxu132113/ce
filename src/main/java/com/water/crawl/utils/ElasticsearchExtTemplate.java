package com.water.crawl.utils;

import org.elasticsearch.client.Client;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * Created by zhangmiaojie on 2017/2/28.
 */
public class ElasticsearchExtTemplate extends ElasticsearchTemplate {
    public ElasticsearchExtTemplate(Client client) {
        super(client);
    }
}
