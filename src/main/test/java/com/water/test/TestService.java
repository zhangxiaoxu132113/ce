package com.water.test;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by zhangmiaojie on 2017/4/13.
 */
public class TestService {

    public static void main(String[] args) {
        String filePath = "D:\\localhost_access_log.2017-04-22.txt";
        File logFile = new File(filePath);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(logFile)));
            int b;
            while((b=reader.read())!=-1){
                String line = reader.readLine();
                line = line.replace("\"","");
                if (StringUtils.isNotBlank(line)) {
                    String[] accessInfos = line.split(" ");
                    for (String info : accessInfos) {
                        System.out.println(info);
                    }
                    System.out.println("-------------------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
