package com.water.ce.web.quartz;

import com.water.ce.crawl.director.CrawlDirector;
import com.water.ce.crawl.director.handler.ITaskCompleteHandler;
import com.water.ce.utils.QueueClientHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * Created by ZMJ on 2017/11/17.
 */
@Service("taskCallBack")
public class TaskCallBack {
    protected final Log logger = LogFactory.getLog(getClass());

    private void init() {
        CrawlDirector.intialize(QueueClientHelper.QUEUEHOST);
        CrawlDirector.registerHandler(new ITaskCompleteHandler() {
            @Override
            public void onTaskSubmitQueueCompleted(String taskId) {
                logger.info("任务【" + taskId + "】提交成功！");
            }

            @Override
            public void onTaskCompleted(String taskId) {
                logger.info("任务【" + taskId + "】处理成功！");
            }

            @Override
            public void onTaskCancelled(String taskId) {

            }
        });
        CrawlDirector.registerWatchQueue(QueueClientHelper.FETCH_ARTICLE_QUEUE);
        logger.info("注册爬虫任务回调方法成功!");
        logger.info("注册爬虫任务队列成功!");
    }
}
