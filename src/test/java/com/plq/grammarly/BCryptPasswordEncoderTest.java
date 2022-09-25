package com.plq.grammarly;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/17
 */
public class BCryptPasswordEncoderTest {
    public static void main(String[] args) {
        String pass = "grammarly2022;;";
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashPass = bcryptPasswordEncoder.encode(pass);
        System.out.println(hashPass);

        boolean flag = bcryptPasswordEncoder.matches("grammarly2021;;", hashPass);
        System.out.println(flag);
        System.out.println(bcryptPasswordEncoder.matches("grammarly2021;;", "$2a$10$SH8NR.SXX6fGJiDxa2RsLO326bAVxaihQppa8HH1fipREHaobLn2W"));
    }
}
