package com.plq.grammarly.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/01
 */
@Component
public class TestTask {

    @Scheduled(fixedRate = 1000L)
    public void test() {
        System.out.println(System.currentTimeMillis());
    }
}
