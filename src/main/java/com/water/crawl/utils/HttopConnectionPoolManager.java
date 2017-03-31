package com.water.crawl.utils;

import org.apache.http.Header;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhangmiaojie on 2017/3/31.
 */
public class HttopConnectionPoolManager {
    public static void main(String[] args) {
//        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//        // 将最大连接数增加到200
//        cm.setMaxTotal(200);
//        // 将每个路由基础的连接增加到20
//        cm.setDefaultMaxPerRoute(20);
//        //将目标主机的最大连接数增加到50
//        HttpHost localhost = new HttpHost("www.yeetrack.com", 80);
//        cm.setMaxPerRoute(new HttpRoute(localhost), 50);
//
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setConnectionManager(cm)
//                .build();
//        HttpClientContext context = HttpClientContext.create();
//        HttpClientConnectionManager connMrg = new BasicHttpClientConnectionManager();
//        HttpClientConnection conn = null;
//        try {
//
//            HttpRoute route = new HttpRoute(new HttpHost("www.yeetrack.com", 80));
//            // 获取新的连接. 这里可能耗费很多时间
//            ConnectionRequest connRequest = connMrg.requestConnection(route, null);
//            // 10秒超时
//            conn = connRequest.get(10, TimeUnit.SECONDS);
//            connRequest.
//            // 如果创建连接失败
//            if (!conn.isOpen()) {
//                // establish connection based on its route info
//                connMrg.connect(conn, route, 1000, context);
//                // and mark it as route complete
//                connMrg.routeComplete(conn, route, context);
//            }
//                // 进行自己的操作.
//            HttpResponse response = conn.receiveResponseHeader();
//            Header[] headers = response.getAllHeaders();
//            for (Header header : headers) {
//                System.out.println(header.getName() +" = "+ header.getValue());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            connMrg.releaseConnection(conn, null, 1, TimeUnit.MINUTES);
//        }
    }
}
