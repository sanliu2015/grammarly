package com.plq.grammarly.controller;

import java.util.List;
import java.util.Set;

import com.plq.grammarly.model.vo.ExchangeParamVO;
import com.plq.grammarly.model.vo.GenParamVO;
import com.plq.grammarly.service.ExchangeCodeService;
import com.plq.grammarly.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/03
 */
@Controller
@RequestMapping("exchangeCode")
public class ExchangeCodeController {

    private final ExchangeCodeService exchangeCodeService;

    public ExchangeCodeController(ExchangeCodeService exchangeCodeService) {
        this.exchangeCodeService = exchangeCodeService;
    }

    /**
     * 生成兑换码
     * @param genParamVO
     * @return
     */
    @PostMapping("/gen")
    @ResponseBody
    public Result gen(@RequestBody @Validated GenParamVO genParamVO) {
        Set<String> numbers = exchangeCodeService.gen(genParamVO);
        return Result.success(numbers);
    }

    /**
     * 兑换码兑换
     * @param exchangeParamVO 兑换参数
     * @return
     */
    @PostMapping("/exchange")
    public Result exchange(@RequestBody @Validated ExchangeParamVO exchangeParamVO) {
        return exchangeCodeService.exchange(exchangeParamVO);
    }

}
