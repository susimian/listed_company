package com.gcloud.listedcompany.util;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {


    public static String httpPostWithForm(String url, Map<String, String> paramsMap) {

        String resultData = "";
        try {
            HttpPost post = new HttpPost(url);
            List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();

            for (String key : paramsMap.keySet()) {
                pairList.add(new BasicNameValuePair(key, paramsMap.get(key)));
            }
            UrlEncodedFormEntity uefe = new UrlEncodedFormEntity(pairList, "utf-8");
            post.setEntity(uefe);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(500000).setConnectionRequestTimeout(500000)
                    .setSocketTimeout(50000).build();
            post.setConfig(requestConfig);
            post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
            post.addHeader("Accept-Encoding", "gzip, deflate");

            HttpResponse response = HttpClientBuilder.create().build().execute(post);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                resultData = EntityUtils.toString(response.getEntity(), "UTF-8");
            } else {

                System.out.println("接口连接失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultData;
    }

    public static String httpGet(String url, Map<String, String> paramsMap) {

        String resultData = "";
        try {

            List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
            if (paramsMap != null) {
                for (String key : paramsMap.keySet()) {
                    pairList.add(new BasicNameValuePair(key, paramsMap.get(key)));
                }
            }

            //转换为键值对
            String paramStr = EntityUtils.toString(new UrlEncodedFormEntity(pairList, Consts.UTF_8));
            url = paramStr.equals("") ? url : url + "?" + paramStr;


            HttpGet get = new HttpGet(url);
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            HttpResponse response = httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                resultData = inputStreamToString(response.getEntity().getContent());
            } else {
                throw new RuntimeException("接口连接失败！");
            }
        } catch (Exception e) {
            throw new RuntimeException("接口连接失败！");
        }
        return resultData;
    }


    private static String inputStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total.toString();
    }


}
