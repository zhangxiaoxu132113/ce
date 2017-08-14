package com.water.crawl.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 一共有两个方法对含有html标签的文本进行过滤得到纯文本
 * 推荐使用Html2Text这个方法
 */
public class HTMLUtil {

    private static Map<String,String> entryMap;

    /**
     * 加载html的实体，保存到HashMap中
     */
    static{
        //创建map集合对象
        entryMap = new HashMap<String,String>();
        //给map集合添加html实体 &lt;
        entryMap.put("&nbsp;", " ");
        entryMap.put("&lt;", "<");
        entryMap.put("&gt;", ">");
        entryMap.put("&amp;", "&");
        entryMap.put("&quot;", "\"");
        entryMap.put("&apos;", "'");
    }

    /**
     * 加载html的实体，保存到HashMap中
     */
    public static void setHTMLEntry(){
        //创建map集合对象
        entryMap = new HashMap<String,String>();
        //给map集合添加html实体
        entryMap.put("&nbsp;", " ");
        entryMap.put("&lt;", "<");
        entryMap.put("&gt;", ">");
        entryMap.put("&amp;", "&");
        entryMap.put("&quot;", "\"");
        entryMap.put("&apos;", "'");
    }
    /**
     * 通过递归删除html标签
     * @param content - 包含HTML标签的内容
     * @author Jack, 2014-05-15.
     * @return 不带HTML标签的文本内容
     */
    public static String removeHtmlTag(String content) {
        Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>");
        Matcher m = p.matcher(content);
        if (m.find()) {
            content = content.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
            content = removeHtmlTag(content);
        }
        return content;
    }

    public static String Html2Text(String inputString){
        String htmlStr = inputString; //含html标签的字符串
        String textStr ="";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;

        try{
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //过滤script标签

            p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //过滤style标签

            p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //过滤html标签

            textStr = htmlStr;
        }catch(Exception e){
            e.printStackTrace();
        }
        return textStr;//返回文本字符串
    }
    /**
     * 该方法可以过滤掉空格回车等字符
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    /**
     * 过滤掉html中的实体
     * @param str
     * @return
     */
    public static String replaceHtmlEntry(String str){

        for(Map.Entry<String, String> entry:entryMap.entrySet()){
            str = str.replaceAll(entry.getKey(), entry.getValue());
        }
        return str;


    }
}