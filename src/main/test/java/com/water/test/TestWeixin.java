package com.water.test;

import com.water.crawl.utils.HttpRequestTool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangmiaojie on 2017/5/9.
 */
public class TestWeixin {
    public static void main(String[] args) {
        System.setProperty("jsse.enableSNIExtension", "false");
//        String isHasMessageUrl = "https://webpush.wx2.qq.com/cgi-bin/mmwebwx-bin/synccheck";
//
//        Map<String, String> headerMap = new HashMap<>();
//        headerMap.put("Accept", "*/*");
//        headerMap.put("Accept-Encoding", "gzip, deflate, sdch, br");
//        headerMap.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
//        headerMap.put("Connection", "keep-alive");
//        headerMap.put("Host", "webpush.wx2.qq.com");
////        headerMap.put("Cookie", "pgv_pvi=183910400; pgv_si=s7008322560; webwxuvid=b542aa5bd8186b54b94e21853b2113e2bc0fe7b262a33b512b01134c1c478fc6882ac0b2f3a09d29e40b9bca64b506b5; webwx_auth_ticket=CIsBEIHJ3a0CGoAB6+lGHgFGMzcgbKyPkjJVe7aw5nm11FyK/+g1s3A2TAsNpS2NrJkIZmuRdZ/bQoNo9QABmIX/T4Pnt5Aqrz+Y9trQlUIUb6qK0Sr2K/yUWUoBltYGzuQb4lhvNVW8K8zUGAOo/uq4EYYAkIVXGHaCrkbfsep7Sh185WELWo1NDuo=; mm_lang=zh_CN; wxloadtime=1494313079_expired; wxpluginkey=1494307801; wxuin=1754539534; wxsid=2W5VpWyKLcxhxt2X; webwx_data_ticket=gScqVbFuBGQqPs+wqXZMFFSb");
//        headerMap.put("Referer", "https://wx2.qq.com/");
//        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
//
//        Map<String, String> paramMap = new HashMap<>();
//        paramMap.put("r", String.valueOf(System.currentTimeMillis()));
//        paramMap.put("skey", "@crypt_7780a5ae_53238ccd8e2c44bfd78e8dc383a1bbab"); //不变
//        paramMap.put("sid", "2W5VpWyKLcxhxt2X");//不变
//        paramMap.put("uin", "1754539534");//不变
//        paramMap.put("deviceid", "e522673349791827");
//        paramMap.put("synckey","1_664368768|2_664369077|3_664368826|11_664368968|13_664330044|201_1494313257|203_1494310826|1000_1494307801|1001_1494307832");
////        paramMap.put("_", String.valueOf(System.currentTimeMillis()));
//        String result = (String) HttpRequestTool.postRequest(isHasMessageUrl, paramMap, headerMap, null, null, false);
//        System.out.println(result);

        test();
    }


    public static void getContactPeople() {
        System.setProperty("jsse.enableSNIExtension", "false");
        String url = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact?";
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "*/*");
        headerMap.put("Accept-Encoding", "gzip, deflate, sdch, br");
        headerMap.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        headerMap.put("Connection", "keep-alive");
        headerMap.put("Host", "webpush.wx2.qq.com");
//        headerMap.put("Cookie", "pgv_pvi=183910400; pgv_si=s7008322560; webwxuvid=b542aa5bd8186b54b94e21853b2113e2bc0fe7b262a33b512b01134c1c478fc6882ac0b2f3a09d29e40b9bca64b506b5; webwx_auth_ticket=CIsBEIHJ3a0CGoAB6+lGHgFGMzcgbKyPkjJVe7aw5nm11FyK/+g1s3A2TAsNpS2NrJkIZmuRdZ/bQoNo9QABmIX/T4Pnt5Aqrz+Y9trQlUIUb6qK0Sr2K/yUWUoBltYGzuQb4lhvNVW8K8zUGAOo/uq4EYYAkIVXGHaCrkbfsep7Sh185WELWo1NDuo=; mm_lang=zh_CN; wxloadtime=1494313079_expired; wxpluginkey=1494307801; wxuin=1754539534; wxsid=2W5VpWyKLcxhxt2X; webwx_data_ticket=gScqVbFuBGQqPs+wqXZMFFSb");
        headerMap.put("Referer", "https://wx2.qq.com/");
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("r", String.valueOf(System.currentTimeMillis()));
        paramMap.put("skey", "@crypt_7780a5ae_53238ccd8e2c44bfd78e8dc383a1bbab"); //不变
        paramMap.put("sid", "2W5VpWyKLcxhxt2X");//不变
        paramMap.put("uin", "1754539534");//不变

        String result = (String) HttpRequestTool.postRequest(url, paramMap, headerMap, null, null, false);
        System.out.println(result);

    }


    public static void test() {
        String url = "https://login.weixin.qq.com/jslogin?appid=wx782c26e4c19acffb&redirect_uri=https%3A%2F%2Fwx.qq.com%2Fcgi-bin%2Fmmwebwx-bin%2Fwebwxnewloginpage&fun=new&lang=zh_CN&_=";
        url = url + System.currentTimeMillis();
        String result = (String) HttpRequestTool.getRequest(url);
        System.out.println(result);
        String[]resultArr = result.split(";");
        Map<String, Object> resultMap = new HashMap<>();
        for (String resultStr : resultArr) {
            String[]tmpArr = resultStr.split("=");
            tmpArr[0] = tmpArr[0].trim();
            tmpArr[1] = tmpArr[1].trim().replace("\"","");
            resultMap.put(tmpArr[0],tmpArr[1]);
        }
        System.out.println();
    }
    //https://github.com/Urinx/WeixinBot

}
