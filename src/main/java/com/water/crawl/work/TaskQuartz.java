package com.water.crawl.work;

import com.water.crawl.utils.HTMLUtil;
import com.water.es.api.Service.IArticleService;
import com.water.uubook.dao.ArticleMapper;
import com.water.uubook.dao.TagArticleMapper;
import com.water.uubook.dao.TagMapper;
import com.water.uubook.model.Article;
import com.water.uubook.model.Tag;
import com.water.uubook.model.TagArticle;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.water.es.entry.AccessLog;
//import com.water.es.entry.IPAddressInfo;

/**
 * Created by zhangmiaojie on 2016/12/2.
 * 任务定时器
 */
public class TaskQuartz {
    private static Log LOG = LogFactory.getLog(TaskQuartz.class);

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private TagArticleMapper tagArticleMapper;

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
        Integer id = 0;
        queryMap.put("count", 100);
        double processValue = 0.0;
        int allValue = articleMapper.countByExample(null);
        LOG.info("开始处理任务，数据总量为" + allValue);
        while (true) {
            queryMap.put("id", id);
            List<Article> articleList = articleMapper.getArticle(queryMap);
            for (Article article : articleList) {
                String description = article.getDescription();
                String content = HTMLUtil.Html2Text(article.getContent());
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

    public void handleArticleTag() {
        Map<String, Object> queryMap = new HashMap<>();
        Integer id = 0;
        queryMap.put("count", 50);
        double processValue = 0.0;
        int allValue = articleMapper.countByExample(null);
        LOG.info("开始处理任务，数据总量为" + allValue);
        while (true) {
            queryMap.put("id", id);
            List<Article> articleList = articleMapper.getArticle(queryMap);
            List<Tag> tagList = tagMapper.selectByExample(null);
            for (Article article : articleList) {
                int tagSize = 0; //获取当前文章一共有多少个标签
                long start = System.currentTimeMillis();
                if (tagSize < 5) {
                    Map<Tag, Integer> result = new HashMap();
                    for (Tag tag : tagList) {
                        int count = calcOfWordOccurrences(tag.getName(), article.getContent());
                        result.put(tag, count);
                    }

                    List<Map.Entry<Tag, Integer>> list = this.sortOfWordOccurrences(result);
                    StringBuilder sb = new StringBuilder();
                    String tags = "";
                    List<Integer> tagIdList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) { // 关联5个标签
                        if ((tagSize - 5) == 0) break;
                        Map.Entry<Tag, Integer> tagMap = list.get(i);
                        int tagId = tagMap.getKey().getId();
                        TagArticle tagArticle = new TagArticle();
                        tagArticle.setArticleId(article.getId());
                        tagArticle.setTagId(tagId);
                        tagArticleMapper.insert(tagArticle);
                        tagSize++;
                        sb.append(tagId + ",");

                        tagIdList.add(tagMap.getKey().getCategoryId());
                    }
                    int categroyId = calcMaxCountNum(tagIdList);
                    if (sb.length() > 1) {
                        tags = sb.substring(0, sb.length()-1);
                    } else {
                        tags = sb.toString();
                    }
                    article.setTags(tags);
                    article.setCategory(categroyId);
                    articleMapper.updateByPrimaryKeySelective(article);
                }
                id = article.getId();
                long end = System.currentTimeMillis();
                System.out.println(end - start);

            }
            processValue += 100.0;
            LOG.info("已处理:" + processValue + ",当前进度=" + ((processValue / allValue) * 100) + "%");
        }



    }
    public List<Map.Entry<Tag, Integer>> sortOfWordOccurrences(Map oldMap) {
        List<Map.Entry<Tag, Integer>> list = new ArrayList<>(oldMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Tag, Integer>>() {
            @Override
            public int compare(Map.Entry<Tag, Integer> arg0,
                               Map.Entry<Tag, Integer> arg1) {
                return arg1.getValue() - arg0.getValue() ;
            }
        });

        return list;
    }

    public int calcOfWordOccurrences(String word, String content) {
        long start = System.currentTimeMillis();
        Pattern p = Pattern.compile(word);
        content = content.toLowerCase();
        Matcher m = p.matcher(content);
        int i = 0;
        while (m.find()) {
            i++;
        }

        long end = System.currentTimeMillis();
        System.out.println(word + " - 出现次数:" + i + ", 耗时:" + (end - start) + "毫秒");
        return i;
    }

    private int calcMaxCountNum(List<Integer> tags) {
        int maxNum = 0;
        int max = 0;
        int tmp = 0;
        for (int i : tags) {
            for (int j : tags) {
                if (i == j) tmp ++;
            }
            if (tmp > max) {
                max = tmp;
                maxNum = i;
            }
            tmp = 0;
        }

        System.out.println("最大值为" + maxNum);
        return maxNum;
    }
}
