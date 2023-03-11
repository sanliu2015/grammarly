package com.plq.grammarly;

import com.twocaptcha.TwoCaptcha;
import com.twocaptcha.captcha.ReCaptcha;

public class RecaptchaTest {

    private static final String apiKey = "0d2b65a9ab1302ee96b0d0683b12bfcd";

    public static void main(String[] args) {
        TwoCaptcha solver = new TwoCaptcha(apiKey);
        ReCaptcha captcha = new ReCaptcha();
        captcha.setUrl("https://www.coursehero.com/login/?login_user_type=&ref=login");
        captcha.setSiteKey("6Lee8D4bAAAAAC3mq6sHfelhKEZJEk667GmJOy4m");
        captcha.setVersion("v2");
        try {
            solver.solve(captcha);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String responseCode = captcha.getCode();
        System.out.println(responseCode);
    }
}
