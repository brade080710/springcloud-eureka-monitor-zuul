package com.jdd.partition.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String args[]) throws IOException {

       String url =  "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx6ab41a09e88436f1&secret=1e8caf565b1f46352d911d74c4557c7c&code=CODE&grant_type=authorization_code";


//        String token = "28_m7LaeSzmGqOBRvrovAnBq1eYQLLz7SoidjhyNWBayyJj0tE71rB77aiE2z7aEUHYcITly4N2gnH2bLw5FAvWrmj13qNXMZuwSkCbc3gBXLscTqSC9osemT9Gjvq8HMyKrIa8kzF3C1nGhPc8CADgAGAKIL";
//
        url ="http://mrw.so/api.htm?format=json&key=5df6f50844bb35541e83acc9@797646c18ff3e3514ffa8e9324f934be&expireDate=2019-12-31&url="
                + URLEncoder.encode("https://www.jdd966.com/kjactive/icbcETCyhtj/authorization-frm.html?partitionId=a78bfefecb7548b5b47a52766a11f918&id=5707bf06-e9b1-4e10-96f4-5831963ff903", "utf-8") ;

//        url="http://mrw.so/api.htm?format=json&key=5df6f50844bb35541e83acc9@797646c18ff3e3514ffa8e9324f934be&expireDate=2019-12-31&url="
//
//                + URLEncoder.encode( "http://192.168.2.153:8086/dist/index.html?partitionId=a78bfefecb7548b5b47a52766a11f918&id=5707bf06-e9b1-4e10-96f4-5831963ff903", "utf-8");

        String str = HttpUtil.get(url);
        JSONObject jsonObject = JSONObject.parseObject(str);
//        CloseableHttpClient client = HttpClients.createDefault();
//        // 通过httpget方式来实现我们的get请求
//        HttpGet httpGet = new HttpGet(requestUrl);
//        // 通过client调用execute方法，得到我们的执行结果就是一个response，所有的数据都封装在response里面了
//        CloseableHttpResponse Response = client.execute(httpGet);
//        HttpEntity entity = Response.getEntity();
//        // 通过EntityUtils 来将我们的数据转换成字符串
//        String str = EntityUtils.toString(entity, "UTF-8");
//        Response.close();


//
//        Map<String, String> params = new HashMap<>();
//        String url = "https://www.jdd966.com/kjactive/icbcETCyhtj/authorization-frm.html";
//        params.put("long_url", "url");
//        params.put("action", "long2short");
//
//
//        String str = HttpUtil.post("https://api.weixin.qq.com/cgi-bin/shorturl?access_token=" + token, JSONObject.toJSONString(params));
        System.out.println(str);




    }
}
