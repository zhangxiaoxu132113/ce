package com.water.crawl.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 网络请求工具类
 * Created by zhangmiaojie on 2017/2/22.
 */
public class HttpRequestTool {

    /**
     * post请求
     *
     * @param requestUrl 请求地址
     * @param paramMap   请求参数
     * @param headerMap  请求头部信息
     * @param cls        json转换后的对象
     * @param isOutInfo  是否打印输出信息
     * @return Object    如果cls参数为null，则返回请求内容字符串
     */
    public static Object postRequest(String requestUrl, Map<String, String> paramMap, Map<String, String> headerMap, CookieConfig cookieConfig, Class cls, boolean isOutInfo) {
        CloseableHttpClient client;
        HttpPost post = null;
        Object obj = null;
        try {
            post = new HttpPost(requestUrl);
            client = HttpClients.custom()
                    .setRetryHandler(setRequestRetryCount(post, requestUrl, 3))
                    .setDefaultCookieStore(setCookies(cookieConfig))
                    .build();
            RequestConfig requestConfig = RequestConfig.custom() // 设置请求超时时间
                    .setConnectionRequestTimeout(10000)
                    .setConnectTimeout(10000)
                    .setSocketTimeout(10000)
                    .build();
            post.setConfig(requestConfig);

            if (paramMap != null && paramMap.entrySet().size() > 0) {
                List params = new ArrayList();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    params.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                post.setEntity(new UrlEncodedFormEntity(params, "UTF-8")); //设置请求参数
            }

            if (headerMap != null && headerMap.entrySet().size() > 0) {
                Header[] headers = new Header[headerMap.entrySet().size()];
                int i = 0;
                for (Map.Entry<String, String> header : headerMap.entrySet()) {
                    headers[i] = new BasicHeader(header.getKey(), header.getValue());
                    i++;
                }
                post.setHeaders(headers); //设置请求头信息
            } else {
                Header header = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
                post.setHeader(header);
            }
            HttpContext localContext = new BasicHttpContext();
            CloseableHttpResponse response = client.execute(post, localContext);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            if (isOutInfo) outHeaderInfo(response, localContext, result);
            if (cls != null) {
                try {
                    obj = JSON.parseObject(result, cls);//解析json为pojo
                } catch (Exception e) {
                    System.out.println("json 解析失败!");
                    e.printStackTrace();
                }
            } else {
                obj = result;
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Object postRequest(String requestUrl) {
        return postRequest(requestUrl, null, null, null, null, false);
    }

    public static String postRequest(String requestUrl, boolean isOutInfo) {
        postRequest(requestUrl, null, null, null, null, isOutInfo);
        return requestUrl;
    }

    public static Object postRequest(String requestUrl, Map<String, String> paramMap, boolean isOutInfo) {
        return postRequest(requestUrl, paramMap, null, null, null, isOutInfo);
    }

    public static Object postRequest(String requestUrl, Map<String, String> paramMap, Map<String, String> headerMap, boolean isOutInfo) {
        return postRequest(requestUrl, paramMap, headerMap, null, null, isOutInfo);
    }

    public static Object postRequest(String requestUrl, Map<String, String> paramMap, Class cls, boolean isOutInfo) {
        return postRequest(requestUrl, paramMap, null, null, cls, isOutInfo);
    }

    /**
     * get请求
     *
     * @param requestUrl 请求地址
     * @param cls        json转换后的对象
     * @param headerMap  请求参数
     * @param isOutInfo  是否打印输出信息
     * @return Object     如果cls参数为null，则返回请求内容字符串
     */
    public static Object getRequest(String requestUrl, Class cls, Map<String, String> headerMap, CookieConfig cookieConfig, boolean isOutInfo) {
        CloseableHttpClient client = null;
        HttpGet get = null;
        Object obj = null;
        try {
            client = HttpClients.custom()
                    .setRetryHandler(setRequestRetryCount(get, requestUrl, 3))
                    .setDefaultCookieStore(setCookies(cookieConfig))
                    .build();
            get = new HttpGet(requestUrl);
            HttpContext localContext = new BasicHttpContext();
            RequestConfig config = RequestConfig.custom()
                    .setConnectionRequestTimeout(60000)
                    .setConnectTimeout(60000)
                    .setSocketTimeout(60000)
                    .build();
            get.setConfig(config);// 设置请求超时时间
            if (headerMap != null && headerMap.entrySet().size() > 0) {
                Header[] headers = new Header[headerMap.entrySet().size()];
                int i = 0;
                for (Map.Entry<String, String> header : headerMap.entrySet()) {
                    headers[i] = new BasicHeader(header.getKey(), header.getValue());
                    i++;
                }
                get.setHeaders(headers); //设置请求头信息
            } else {
                Header header = new BasicHeader("User-Agent", UARandomUtil.getPCUA());
                get.setHeader(header);
            }
            CloseableHttpResponse response = client.execute(get, localContext);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            if (isOutInfo) outHeaderInfo(response, localContext, result);
            if (cls != null) {
                try {
                    obj = JSON.parseObject(result, cls);//解析json为pojo
                } catch (Exception e) {
                    System.out.println("json 解析失败!");
                    if (isOutInfo) e.printStackTrace();
                }
            } else {
                obj = result;
            }
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Object getRequest(String requestUrl) {
        return getRequest(requestUrl, null, null, null, false);
    }

    public static Object getRequest(String requestUrl, boolean isOutInfo) {
        return getRequest(requestUrl, null, null, null, isOutInfo);
    }

    public static Object getRequest(String requestUrl, Map<String, String> headers, boolean isOutInfo) {
        return getRequest(requestUrl, null, headers, null, isOutInfo);
    }

    public static Object getRequest(String requestUrl, Class cls, boolean isOutInfo) {
        return getRequest(requestUrl, cls, null, null, isOutInfo);
    }

    public static Object getRequestByCookies(String requestUrl, CookieConfig cookieConfig, boolean isOutInfo) {
        return getRequest(requestUrl, null, null, cookieConfig, isOutInfo);
    }

    /**
     * 设置请求次数
     *
     * @param retryCount 请求次数
     * @return HttpRequestRetryHandler
     */
    public static HttpRequestRetryHandler setRequestRetryCount(final HttpRequestBase httpRequestBase, final String requestUrl, final int retryCount) {
        HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext httpContext) {
                HttpRequest request = (HttpRequest) httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
                System.out.println("访问失败【" + executionCount + "】尝试重新访问 -----> " + requestUrl + "");

                if (executionCount > retryCount) {
                    abortHttpRequest(httpRequestBase);
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 服务停掉则重新尝试连接
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// SSL异常不需要重试
                    abortHttpRequest(httpRequestBase);
                    return false;
                }

                boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
                if (!idempotent) {// 请求内容相同则重试
                    return true;
                }
                abortHttpRequest(httpRequestBase);
                return false;
            }
        };
        return requestRetryHandler;
    }

    public static void abortHttpRequest(HttpRequestBase requestBase) {
        if (requestBase instanceof HttpPost) {
            ((HttpPost) requestBase).abort();
        } else if (requestBase instanceof HttpGet) {
            ((HttpGet) requestBase).abort();
        } else {
            new RuntimeException("参数不合法！");
        }

    }

    /**
     * 设置请求cookie
     *
     * @param cookieConfig cookie配置类
     * @return CookieStore
     */
    public static CookieStore setCookies(CookieConfig cookieConfig) {
        CookieStore cookieStore = new BasicCookieStore();
        if (cookieConfig != null && (cookieConfig.getCookirMap() != null && cookieConfig.getCookirMap().entrySet().size() > 0)) {
            for (Map.Entry<String, String> cookieParam : cookieConfig.getCookirMap().entrySet()) {
                BasicClientCookie cookie = new BasicClientCookie(cookieParam.getKey(), cookieParam.getValue());
                cookie.setDomain(cookieConfig.getDomain());
                cookie.setExpiryDate(cookieConfig.getExpiryDate());
                cookieStore.addCookie(cookie);
            }
        }
        return cookieStore;
    }

    /**
     * 打印输出响应头和请求头信息
     *
     * @param response     CloseableHttpResponse
     * @param localContext HttpContext
     */
    private static void outHeaderInfo(CloseableHttpResponse response, HttpContext localContext, String result) {
        outResponseHeaderInfo(response);
        outRequestHeaderInfo(localContext);
        System.out.println(result);
    }


    /**
     * 输出响应头部信息
     *
     * @param response CloseableHttpResponse
     */
    private static void outResponseHeaderInfo(CloseableHttpResponse response) {
        if (response != null) {
            int statusCode = response.getStatusLine().getStatusCode();
            Header[] headers = response.getAllHeaders();
            System.out.println("------------------- 响应头信息 -------------------");
            System.out.println("响应状态码 : " + statusCode);
            for (Header header : headers) {
                System.out.println(header.getName() + " : " + header.getValue());
            }
            System.out.println("---------------------- end ----------------------");
        }
    }

    /**
     * 输出请求头信息
     *
     * @param localContext HttpContext
     */
    private static void outRequestHeaderInfo(HttpContext localContext) {
        if (localContext != null) {
            HttpRequest request = (HttpRequest) localContext
                    .getAttribute(ExecutionContext.HTTP_REQUEST);
            Header[] headers = request.getAllHeaders();
            System.out.println("------------------- 请求头信息 -------------------");
            for (Header header : headers) {
                System.out.println(header.getName() + " : " + header.getValue());
            }
            System.out.println("---------------------- end ----------------------");
        }
    }

    public static void main(String[] args) throws Exception {
        getJavaThreadBook();
    }

    public static void getJavaThreadBook() {
        String root_url = "http://ifeve.com/java-7-concurrency-cookbook/";
        String pageHtml = (String) getRequest(root_url);
        Document doc = Jsoup.parse(pageHtml);

        Elements baseEle = doc.select(".post_content");
        Elements titleEles = baseEle.select("h3");
        Elements secondTitleEles = baseEle.select("ol");
        int total = 8;
        for (int i = 0; i < total; i++) {
            Element titleEle = titleEles.get(i + 1);
            Element secondTitleEle = secondTitleEles.get(i);
            System.out.println(titleEle.text());
            Elements linkEles = secondTitleEle.select("li > a");
            for (Element linkEle : linkEles) {
                System.out.println(linkEle.text() + "\t\t" + linkEle.absUrl("href"));
            }

        }
    }


}