package com.water.crawl.utils;

import org.apache.commons.lang.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by mrwater on 2016/11/27.
 */
public class StringUtil {
    public static Set<String> staticResourceNames = new HashSet<String>();
    static {
        initStaticResourceNames();
    }

    public static void initStaticResourceNames() {
        String filterStaticResource = Constants.getFILTER_STATIC_RESOURCE();
        String[] resources = filterStaticResource.split(",");
        for (String resource : resources) {
            staticResourceNames.add(resource);
        }
    }

    /**
     * 判断是否访问的是静态资源
     */
    public static boolean isRequestStaticResourceUrl(String url) {
        if (StringUtils.isNotBlank(url)) {
            for (String srn : staticResourceNames) {
                if (url.lastIndexOf(srn) != -1) return true;
            }
        }
        return false;
    }

    /**
     * 获取网站的一级域名
     */
    public static String getTopDomainWithoutSubdomain(String url) throws MalformedURLException {
        String host = new URL(url).getHost().toLowerCase();// 此处获取值转换为小写
        Pattern pattern = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
        Matcher matcher = pattern.matcher(host);
        while (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     * unicode编码
     */
    public static String decodeUnicode(String str) {
        Charset set = Charset.forName("UTF-16");
        Pattern p = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher m = p.matcher(str);
        int start = 0;
        int start2 = 0;
        StringBuffer sb = new StringBuffer();
        while (m.find(start)) {
            start2 = m.start();
            if (start2 > start) {
                String seg = str.substring(start, start2);
                sb.append(seg);
            }
            String code = m.group(1);
            int i = Integer.valueOf(code, 16);
            byte[] bb = new byte[4];
            bb[0] = (byte) ((i >> 8) & 0xFF);
            bb[1] = (byte) (i & 0xFF);
            ByteBuffer b = ByteBuffer.wrap(bb);
            sb.append(String.valueOf(set.decode(b)).trim());
            start = m.end();
        }
        start2 = str.length();
        if (start2 > start) {
            String seg = str.substring(start, start2);
            sb.append(seg);
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        String url = "https://www.oschina.net/blog?classification=428602";
        Map<String, String> getParams = new HashMap<String, String>();
        String[] strs = url.split("[?]");
        if (strs.length == 2) {
            String str = strs[1];
            strs = str.split("&");
            if (strs.length > 0) {
                for (int i = 0; i < strs.length; i++) {
                    String[] queryArr = strs[i].split("=");
                    if (queryArr.length == 2){
                        getParams.put(queryArr[0],queryArr[1]);
                    }
                }
            }
        }
    }

    /**
     * 获取url的参数
     */
    public static Map<String, String> getParamWithUrl(String url) {
        Map<String, String> getParams = null;
        if (StringUtils.isNotBlank(url)) {
            getParams = new HashMap<String, String>();
            String[] strs = url.split("[?]");
            if (strs.length == 2) {
                String str = strs[1];
                strs = str.split("&");
                if (strs.length > 0) {
                    for (int i = 0; i < strs.length; i++) {
                        String[] queryArr = strs[i].split("=");
                        if (queryArr.length == 2){
                            getParams.put(queryArr[0],queryArr[1]);
                        }
                    }
                }
            }
        }
        return getParams;
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}
