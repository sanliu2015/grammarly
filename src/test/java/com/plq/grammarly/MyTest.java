package com.plq.grammarly;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import java.util.List;
import java.util.Map;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/01
 */
public class MyTest {

    public static void main(String[] args) {
        HttpRequest httpRequest = HttpUtil.createGet("https://www.grammarly.com/signin");
        HttpResponse httpResponse = httpRequest.execute();
        Map<String, List<String>> headerMap = httpResponse.headers();
        for (Map.Entry<String, List<String>> entry : headerMap.entrySet()) {
            System.out.println("键:" + entry.getKey() + "，值:" + entry.getValue());
        }
    }
}
