package com.plq.grammarly.selenium;

import cn.hutool.json.JSONObject;
import com.plq.grammarly.model.entity.QuestionExchangeCode;

public interface SeleniumService {

    void initEdgeSession();

    void destoryEdgeSession();

    /**
     * 解锁coursehero问题并下载
     * @param questionExchangeCode
     * @return
     */
    JSONObject unlockCourseHeroQuestion(QuestionExchangeCode questionExchangeCode);

    /**
     * 破解 google recaptcha v2
     * @return
     */
    boolean crackGoogleRecaptchaV2();

}
