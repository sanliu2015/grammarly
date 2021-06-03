package com.plq.grammarly.controller;

import com.plq.grammarly.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/03
 */
@Controller
@RequestMapping("exchange")
public class ExchangeCodeController {

    @PostMapping()
    public Result gen() {
        return Result.success();
    }
}
