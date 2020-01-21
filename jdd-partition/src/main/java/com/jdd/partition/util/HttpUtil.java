package com.jdd.partition.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * http 工具类
 */
public class HttpUtil {

    public HttpUtil() throws IOException {
    }

    public static String post(String requestUrl, String accessToken, String params)
            throws Exception {
        String contentType = "application/x-www-form-urlencoded";
        return HttpUtil.post(requestUrl, accessToken, contentType, params);
    }

    /**
     * @throws IOException
     * @Title: post
     * @Description: TODO(httpPost请求)
     * @param: @param requestUrl
     * @param: @param params
     * @param: @return
     * @param: @throws Exception
     * @return: String
     * @throws
     */
    public static String post(String requestUrl,  String params) throws IOException
    {
        String encoding = "UTF-8";
    	String contentType = "application/json";
        URL url = null;
		url = new URL(requestUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection;
		connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", contentType);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        // 得到请求的输出流对象
        OutputStreamWriter  out = new OutputStreamWriter(connection.getOutputStream(),"UTF-8");
        out.append(params);
        out.flush();
        out.close();
        // 建立实际的连接
        connection.connect();
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), encoding));
        StringBuffer  result = new StringBuffer("");
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result.append(getLine);
        }
        in.close();
        return result.toString();
    }

    /**
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        // 获取http客户端
        CloseableHttpClient client = HttpClients.createDefault();
        // 通过httpget方式来实现我们的get请求
        HttpGet httpGet = new HttpGet(url);
        // 通过client调用execute方法，得到我们的执行结果就是一个response，所有的数据都封装在response里面了
        CloseableHttpResponse Response = client.execute(httpGet);
        HttpEntity entity = Response.getEntity();
        // 通过EntityUtils 来将我们的数据转换成字符串
        String str = EntityUtils.toString(entity, "UTF-8");
        Response.close();
        return str;


    }

    public static String post(String requestUrl, String accessToken, String contentType, String params)
            throws Exception {
        String encoding = "UTF-8";
        if (requestUrl.contains("nlp")) {
            encoding = "GBK";
        }
        return HttpUtil.post(requestUrl, accessToken, contentType, params, encoding);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params, String encoding)
            throws Exception {
        String url = requestUrl + "?access_token=" + accessToken;
        return HttpUtil.postGeneralUrl(url, contentType, params, encoding);
    }

    public static String postGeneralUrl(String generalUrl, String contentType, String params, String encoding)
            throws Exception {

        URL url = new URL(generalUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(params.getBytes(encoding));
        out.flush();
        out.close();

        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> headers = connection.getHeaderFields();
        // 遍历所有的响应头字段
//        for (String key : headers.keySet()) {
//            System.err.println(key + "--->" + headers.get(key));
//        }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), encoding));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
        return result;
    }
}
