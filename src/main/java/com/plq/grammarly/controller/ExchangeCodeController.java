package com.plq.grammarly.controller;

import java.util.Map;
import java.util.Set;

import com.plq.grammarly.model.vo.ExchangeCodeQueryVO;
import com.plq.grammarly.model.vo.ExchangeParamVO;
import com.plq.grammarly.model.vo.GenParamVO;
import com.plq.grammarly.service.ExchangeCodeService;
import com.plq.grammarly.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/03
 */
@Controller
public class ExchangeCodeController {

    private final ExchangeCodeService exchangeCodeService;

    public ExchangeCodeController(ExchangeCodeService exchangeCodeService) {
        this.exchangeCodeService = exchangeCodeService;
    }

    @GetMapping(value = { "index", "", "/" } )
    public String index() {
        return "index";
    }

    @GetMapping("/exchangeCode")
    public String exchangeCode() {
        return "exchangeCode";
    }

    @GetMapping("/exchangeCodes")
    @ResponseBody
    public Map<String, Object> pageQuery(ExchangeCodeQueryVO exchangeCodeQueryVO) {
        return exchangeCodeService.pageQuery(exchangeCodeQueryVO);
    }

    /**
     * 生成兑换码
     * @param genParamVO
     * @return
     */
    @PostMapping("/exchangeCode/gen")
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
    @PostMapping("/exchangeCode/exchange")
    @ResponseBody
    public Result exchange(@RequestBody @Validated ExchangeParamVO exchangeParamVO) {
        return exchangeCodeService.exchange(exchangeParamVO);
    }



}
