//package com.water.crawl.work;
//
//import com.water.crawl.core.HttpCrawlClient;
//import com.water.crawl.core.HttpCrawlResponse;
//import com.water.crawl.db.model.Headers;
//import com.water.crawl.db.model.Weibo;
//import com.water.crawl.utils.Constants;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by mrwater on 2016/12/4.
// * 技术障碍
// * 1，每一次访问新浪微博个人首页的时候都是没有将所有的数据显示出来
// * 2，优化，在header类改为headers，在其里面加header
// * 3，获取高清的图片，http://ww2.sinaimg.cn/mw690/be3489c6gw1f3g6for6vyj21f70qojyv.jp
// * 以这种方式结尾的是获取高清的图片的，thumb结尾的就是缩略图片的
// */
//public class FetchProfileWeibo {
//    public static int pageNum = 1;
//    public static String cookie = "SINAGLOBAL=4266904063047.0854.1481429830519; ULV=1481433851059:2:2:2:6197424504093.201.1481433850888:1481429830540; UOR=,,login.sina.com.cn; YF-Page-G0=f994131fbcce91e683b080a4ad83c421; SCF=Ag1OyX7lznO1Mju-wxWNJGnqxDvoZwcEgacRckRG7qPdxksyJSEr56rbYpujyC4wlTjVHkY3gKWCj5-GVDSk5Js.; SUB=_2A251bA62DeRxGeVP4lMQ8SrNwz6IHXVWGGd-rDV8PUNbmtBeLRCtkW9ugf8-AKCoiBVIp_xNUiJbnKYmJQ..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5B6JSLT7GMcxB7iP-Z.QyU5JpX5KMhUgL.Foep1K2peKBp1hz2dJLoI7LSMoe_Hc8X9Pzt; SUHB=0M2F8d0uiCspRV; ALF=1514779237; SSOLoginState=1483243239";
//    public static String profileUrl = "http://weibo.com/u/3191114182?is_all=1&page=" + pageNum; // is_all = 1 说明一次性加载该页面的所有微博，而不是异步加载
//    public static String profileUrl2 = "http://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6&domain=100505&is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page="+pageNum+"&pagebar=0&pl_name=Pl_Official_MyProfileFeed__22&id=1005053191114182&script_uri=/u/3191114182&feed_type=0&pre_page="+pageNum+"&domain_op=100505&__rnd=";
//    public static String profileUrl3 = "http://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6&domain=100505&is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page="+pageNum+"&pagebar=1&pl_name=Pl_Official_MyProfileFeed__22&id=1005053191114182&script_uri=/u/3191114182&feed_type=0&pre_page="+pageNum+"&domain_op=100505&__rnd=";
//
//
//    public static void main(String[] args) {
//        FetchProfileWeibo fetchProfileWeibo = new FetchProfileWeibo();
//        fetchProfileWeibo.fetchProfileWeiboContent();
//    }
//
//    /**
//     * 抓取用户的微博数据
//     */
//    public List<Weibo> fetchProfileWeiboContent() {
//        HttpCrawlClient client = null;
//        List<String> htmlBodys = new ArrayList<String>();
//        List<Weibo> weiboList = new ArrayList<Weibo>();
//        try {
//            client = HttpCrawlClient.newInstance();
//            Headers headerList = Headers.Header.newInstance()
//                    .add("Cookie", cookie)
//                    .add("Host", Constants.RequestHeaders.HOST_WEIBO)
//                    .add("User-Agent", Constants.RequestHeaders.USER_AGENT)
//                    .builder();
//            HttpCrawlResponse response = client.executeGetRequest(profileUrl, headerList);
//            if (response.isSuccessful()) {
//                String body = response.getPageBody();
//                htmlBodys.add(body);
//                response = client.executeGetRequest(profileUrl2 + System.currentTimeMillis(), headerList);
//                String secondBody = response.getPageBody();
//                htmlBodys.add(secondBody);
//                response = client.executeGetRequest(profileUrl3 + System.currentTimeMillis(), headerList);
//                String body3 = response.getPageBody();
//                htmlBodys.add(body3);
//                Document doc = Jsoup.parse(body);
//                String origin_htmlBody = "";
//                Elements tmp_elements = doc.getElementsByTag("script");
//                for (Element element : tmp_elements) {
//                    origin_htmlBody += element.html() +"\n";
//                }
//                origin_htmlBody += "\n"+secondBody;
//                origin_htmlBody += "\n"+body3;
//
//                //解析微博内容
//                Document document = Jsoup.parse(origin_htmlBody);
//                Elements elements = getWeiboDetailList(document);
//                System.out.println("开始解析！！！\n 当前页面一共有" + elements.size() + "条数据!!");
//                Weibo weibo = null;
//                for (Element element : elements) {
//                    weibo = new Weibo();
//                    getWeiboInfo(element,weibo);
//                    getWeiboForm(element,weibo);
//                    getWeiboText(element,weibo);
//                    weiboList.add(weibo);
//                    System.out.println("\n\n\n");
//                }
//                return weiboList;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//    public static Elements getWeiboDetailList(Document document) {
//        if (document == null) throw new RuntimeException("文档对象不能为空！");
//        return document.select("div.WB_detail");
//    }
//
//    public static Element getWeiboInfo(Element element, Weibo weibo) {
//        Element ele = element.select("div.WB_info").get(0);
//        Elements eles = ele.getElementsByTag("a");
//        ele = eles.get(0);
////        weibo.setUserId();
//        System.out.println("用户" + ele.attr("abs:href")); // 获取a标签的绝对路径
//        System.out.println("用户昵称" + ele.html());
//        ele.outerHtml();
//        return ele;
//    }
//
//    public static void getWeiboForm(Element element, Weibo weibo) {
//        Element ele = element.select("div.WB_from").get(0);
//        Elements eles = ele.getElementsByTag("a");
//        System.out.println("发布时间 " + eles.get(0).html());
////        weibo.setCreateAt();
//        System.out.println("设备来源 " + eles.get(1).html());
//    }
//
//    public static void getWeiboText(Element element, Weibo weibo) {
//        Element ele = element.select("div.WB_text").get(0);
//        weibo.setText(ele.html());
//        System.out.println("微博内容 " + ele.html());
//    }
//
//}
