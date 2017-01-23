package com.water.crawl.db.service.impl.statistics;

import com.water.crawl.db.dao.statistics.WeiboMapper;
import com.water.crawl.db.service.interfaces.statistics.WeiboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(WeiboService.SERVICE_NAME)
public class WeiboServiceImpl implements WeiboService {
    @Resource
    private WeiboMapper weiboMapper;
}