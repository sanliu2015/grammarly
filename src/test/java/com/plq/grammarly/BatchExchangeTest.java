package com.plq.grammarly;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BatchExchangeTest {

    public static void main(String[] args) throws InterruptedException {
        long beiginTime = System.currentTimeMillis();
        int count = 40;
        final CountDownLatch latch = new CountDownLatch(count);
        ExecutorService threadPool = Executors.newFixedThreadPool(16);
        for (int i=0; i<count; i++) {
            int finalI = i;
            threadPool.submit(() -> {
                JSONObject jsonObject = new JSONObject().putOpt("number", "567mj3bjc4eyj1ct").putOpt("email", "717" + finalI + "@q123.com");
                HttpResponse httpResponse = HttpUtil.createPost("localhost:8899/grammarly/exchangeCode/exchange")
                        .body(JSONUtil.toJsonStr(jsonObject))
                        .execute();
                latch.countDown();
            });
        }
        latch.await();  // wait for all to finish
        System.out.println("所有子线程均执行完毕,用时:" + (System.currentTimeMillis()-beiginTime) + "毫秒");
        threadPool.shutdown();
    }
}
