package com.water.crawl.utils.lang;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mrwater on 2016/11/27.
 */
public class StringUtil {
    public static Calendar calendar = Calendar.getInstance();

    /**
     * 获取网站的一级域名
     *
     * @param url
     * @return
     * @throws MalformedURLException
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
     *
     * @param str
     * @return
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

    /**
     * 根据URL链接获取参数
     *
     * @param url
     * @return
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
                        if (queryArr.length == 2) {
                            getParams.put(queryArr[0], queryArr[1]);
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

    /**
     * 生成11长度的uid
     */
    public static String generateShortUid() {
        String uid = String.valueOf(calendar.getTime().getTime()); //13位数字
        uid = uid.substring(5, uid.length());                      //取后面8位
        uid = new java.util.Random().nextInt(900)+100 + uid;       //随机生成3位 拼接后 8位数字
        return uid;
    }

    public static void main(String[] args) throws MalformedURLException {
//
    }
}
