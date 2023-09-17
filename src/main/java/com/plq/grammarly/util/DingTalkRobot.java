package com.plq.grammarly.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class DingTalkRobot {

    private final static String SEND_URL = "https://oapi.dingtalk.com/robot/send?access_token=a87b847319ffd0f45c2cf5c553dcc2be838ac44a83a9eb4a379e86ce3b961d8e";

    public static void sendMsg(String message) {
        message = "【grammarly报警】" + message;
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("msgtype", "text").putOpt("text", new JSONObject().putOpt("content", message));
        HttpUtil.createPost(SEND_URL)
                .contentType("application/json")
                .body(JSONUtil.toJsonStr(jsonObject))
                .execute();
    }

    public static void sendMarkdownMsg(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("msgtype", "markdown").putOpt("markdown", new JSONObject().putOpt("title", "grammarly报警").putOpt("text", message));
        HttpUtil.createPost(SEND_URL)
                .contentType("application/json")
                .body(JSONUtil.toJsonStr(jsonObject))
                .execute();
    }

}
