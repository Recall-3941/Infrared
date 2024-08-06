package net.ccbluex.liquidbounce.utils;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpClientUtil {


    /**
     * 模拟发送url Get 请求
     * @param url
     * @return
     */
    public String requestByGetMethod(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        StringBuilder entityStringBuilder = null;
        try {
            HttpGet get = new HttpGet(url);
            CloseableHttpResponse httpResponse = null;
            httpResponse = httpClient.execute(get);
            try {
                HttpEntity entity = httpResponse.getEntity();
                entityStringBuilder = new StringBuilder();
                if (null != entity) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"), 8 * 1024);
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        entityStringBuilder.append(line + "/n");
                    }
                }
            } finally {
                httpResponse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return entityStringBuilder.toString();
    }

    public static void main(String qq) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String url = "https://zy.xywlapi.cc/qqapi?qq="+qq;
        String res = null;
        try {
            res = httpClientUtil.requestByGetMethod(url).split("/n")[0];
            ClientUtils.displayChatMessage(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
