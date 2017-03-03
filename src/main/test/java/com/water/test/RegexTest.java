package com.water.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangmiaojie on 2017/1/26.
 */
public class RegexTest {
    public static void main(String[] args) {
//        Pattern pattern = Pattern.compile("");
//        Matcher matcher = pattern.matcher("");
//        while (matcher.find()) {
//            System.out.println(matcher.group());
//        }
//        test01();

        String url = "123";
        url = url.substring(1,2);
        System.out.println(url);
    }


    public static void test01() {
        // 按指定模式在字符串查找
        String line = "This order was placed for QT3000! OK?";
        String pattern = "(\\D*)(\\d+)(.*)";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(0) );
            System.out.println("Found value: " + m.group(1) );
            System.out.println("Found value: " + m.group(2) );
            System.out.println("Found value: " + m.group(3) );
        } else {
            System.out.println("NO MATCH");
        }
    }
}
