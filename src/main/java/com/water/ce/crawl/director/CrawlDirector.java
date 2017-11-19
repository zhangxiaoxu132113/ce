package com.water.ce.crawl.director;

import com.water.ce.crawl.director.handler.ITaskCompleteHandler;
import com.water.ce.web.model.dto.CrawlingTask;
import com.xpush.queue.QueueClient;
import com.xpush.queue.QueueManager;
import com.xpush.serialization.protobuf.ProtoEntity;
import com.xpush.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangmiaojie on 2016/12/2.
 */
public class CrawlDirector {
    public static List<String> watchList = null;
    public static ITaskCompleteHandler handler;
    public static QueueClient client;
    private static ScheduledExecutorService executor;
    private static InitialLock lock = new InitialLock();

    public static void intialize(final String hosts) {
        lock.doInit(new Runnable() {
            @Override
            public void run() {
                CrawlDirector.client = QueueManager.getClient(hosts, "");
                watchList = new ArrayList<>(30);  //默认只监听队列数量 20
                executor = Executors.newScheduledThreadPool(2);
            }
        });
    }

    public static void registerHandler(ITaskCompleteHandler handler) {
        CrawlDirector.handler = handler;
    }

    public static void registerWatchQueue(final String queueName) {
        try {
            if (watchList.contains(queueName)) return;
            watchList.add(queueName);
            executor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    CrawlDirector.doWatchQueue(queueName);
                }

            }, 10L, 60L, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void doWatchQueue(String queueName) {
        Long len = client.queueLen(queueName);
        if (len.longValue() == 0L) {
            byte[] data = client.queueHeadX(queueName, getTaskListName(queueName));
            if (data != null) {
                CrawlingTask crawlingTask = new CrawlingTask();

                try {
                    ProtoEntity.parseFrom(crawlingTask, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                len = client.queueLen(getTaskQueueName(queueName, crawlingTask.getTaskId()));
                if (len == 0L) {
                    System.out.println("任务执行完成！");
                    client.dequeueNoBlockX(queueName, getTaskListName(queueName));
                    if (handler != null) {
                        handler.onTaskCompleted(crawlingTask.getTaskId());
                    }
                    client.enqueue(getTaskListCompletedName(queueName), formatByte(crawlingTask.getTaskId()));
                } else {
                    client.rename(getTaskQueueName(queueName, crawlingTask.getTaskId()), queueName);
                }
            }
        }
    }

    public static void submitTask(String queueName, CrawlingTask task) {
        boolean isWatch = false;
        Iterator iterator = watchList.iterator();

        while (iterator.hasNext()) {
            if (iterator.next().equals(queueName)) {
                isWatch = true;
            }
        }
        if (isWatch) {
            if (task != null && StringUtils.isNotBlank(queueName) && task.getDatas() != null && task.getTaskId().length() > 0) {
                try {
                    Iterator<ProtoEntity> var4 = task.getDatas().iterator();
                    while (var4.hasNext()) {
                        byte[] data = var4.next().toByteArray();
                        client.enqueue(getTaskQueueName(queueName, task.getTaskId()), data);
                    }
                    client.enqueueX(queueName, getTaskListName(queueName), task.toByteArray());
                    client.enqueue(getTaskListSubmitedName(queueName), formatByte(task.getTaskId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static byte[] formatByte(String taskId) {
        return String.format("%s(%s)", new Object[]{taskId, DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")}).getBytes();
    }

    public static String getTaskQueueName(String queueName, String taskId) {
        return queueName + "_" + taskId;
    }

    public static String getTaskListName(String queueName) {
        return queueName + "_task";
    }

    public static String getTaskListCompletedName(String queueName) {
        return queueName + "_complated";
    }

    public static String getTaskListSubmitedName(String queueName) {
        return queueName + "_submited";
    }

}
