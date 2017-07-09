package com.water.crawl.work;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.water.crawl.db.dao.ITArticleMapper;
import com.water.crawl.db.model.ITArticle;
import com.water.crawl.utils.HtmlUtil;
import com.water.es.api.Service.IArticleService;
//import com.water.es.entry.AccessLog;
//import com.water.es.entry.IPAddressInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by zhangmiaojie on 2016/12/2.
 * 任务定时器
 */
public class TaskQuartz {
    private static Log LOG = LogFactory.getLog(TaskQuartz.class);

    @Autowired
    private ITArticleMapper articleMapper;

    @Resource(name = "esArticleService")
    private IArticleService esArticleService;

    public static void main(String[] args) {
        double processValue = 150.0;
        int allValue = 2000;
        System.out.println("已处理:" + processValue + ",当前进度=" + (int) ((processValue / allValue) * 100) + "%");
    }

    /**
     * 统计前一天的服务器访问日志
     */
    public void handleAccessLog() {
//        String filePattern = "localhost_access_log.%s.txt";
//        String currentdDateStr = DateUtil.DATE_FORMAT_YMD.format(new Date(DateUtil.getDayTimeInMillis(new Date(), -1)));
//        String filePath = Constants.ACCESS_LOG_PATH + String.format(filePattern, currentdDateStr);
//        File logFile = new File(filePath);
//        if (!logFile.exists()) {
//            LOG.warn("【" + currentdDateStr + "】 - 当天没有需要分析的日志！");
//            return;
//        }
//        try {
//            LOG.info("开始分析访问日志......");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    new FileInputStream(logFile)));
//            int b;
//            while ((b = reader.read()) != -1) {
//                AccessLog log = new AccessLog();
//                String line = reader.readLine();
//                line = line.replace("\"", "");
//                if (StringUtils.isNotBlank(line)) {
//                    String[] accessInfos = line.split(" ");
//                    String requestUrl = accessInfos[4];
//                    if (StringUtil.isRequestStaticResourceUrl(requestUrl)) continue; // 不记录静态文件访问记录
//                    String dateStr = accessInfos[1] + " " + accessInfos[2];
//                    dateStr = dateStr.replace("[", "");
//                    dateStr = dateStr.replace("]", "");
//                    Date accessDate = DateUtil.DATE_FORMAT_ZONE.parse(dateStr);
//                    log.setAccessDate(accessDate);
//
//                    //调用ip地址查询接口，设置ip的详细信息
//                    String ip = accessInfos[0];
//                    String ipAddrQueryApi = String.format(Constant.API.QUERY_IP, ip);
//                    String resultJson = (String) HttpRequestTool.getRequest(ipAddrQueryApi, false);
//                    if (StringUtils.isNotBlank(resultJson)) {
//                        JSONObject jsonObj = JSONObject.parseObject(resultJson);
//                        JSONObject dataObj = jsonObj.getJSONObject("data");
//                        IPAddressInfo ipAddressInfo = JSON.parseObject(dataObj.toJSONString(), IPAddressInfo.class);
//                        log.setIpAddressInfo(ipAddressInfo);
//                    }
//
//                    log.setRequestMethod(accessInfos[3]);
//                    log.setReqeuestUrl(requestUrl);
//                    log.setStatus(Integer.valueOf(accessInfos[6]));
//                    int sentByte = 0;
//                    if (!accessInfos[7].equals("-")) {
//                        sentByte = Integer.valueOf(accessInfos[7]);
//                    }
//                    log.setSentBytes(Integer.valueOf(sentByte));
//                    log.setProcessRequestTime(Long.valueOf(accessInfos[8]));
//                    log.setCreateOn(System.currentTimeMillis());
//                    ElasticSearchUtils.addDocument(JSONObject.toJSONString(log),
//                            "admin",
//                            "access-log",
//                            UUID.randomUUID().toString());
//                    LOG.info("访问日志分析结束......");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    /**
     * 全量的把数据的文章导入到es中
     */
    public void importArticle2Es() {
        Map<String, Object> queryMap = new HashMap<>();
        String id = "";
        queryMap.put("count", 100);
        double processValue = 0.0;
        int allValue = articleMapper.countByExample(null);
        LOG.info("开始处理任务，数据总量为" + allValue);
        while (true) {
            queryMap.put("id", id);
            List<ITArticle> articleList = articleMapper.getAllArticle(queryMap);
            for (ITArticle article : articleList) {
                String description = article.getDescription();
                String content = HtmlUtil.Html2Text(article.getContent());
                if (StringUtils.isBlank(description)) {// 如果description内容为空，则设置description的内容
                    if (content.length() >= 255) {
                        description = content.substring(0, 255);
                    } else {
                        description = content;
                    }
                    article.setDescription(description);
                    articleMapper.updateByPrimaryKeySelective(article);
                }

                article.setContent(content);
                com.water.es.entry.ITArticle esArticle = new com.water.es.entry.ITArticle();
                BeanUtils.copyProperties(article, esArticle);
                esArticleService.addArticle(esArticle);
                id = article.getId();
            }

            processValue += 100.0;
            LOG.info("已处理:" + processValue + ",当前进度=" + ((processValue / allValue) * 100) + "%");
        }
    }
}
