package com.plq.grammarly.selenium;

import cn.hutool.json.JSONObject;
import com.plq.grammarly.model.entity.QuestionExchangeCode;

public interface SeleniumService {

    void initEdgeSession();

    /**
     * 解锁coursehero问题并下载
     * @param questionExchangeCode
     * @return
     */
    JSONObject unlockCourseHeroQuestion(QuestionExchangeCode questionExchangeCode);
}