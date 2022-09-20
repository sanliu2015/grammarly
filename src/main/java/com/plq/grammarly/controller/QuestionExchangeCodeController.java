package com.plq.grammarly.controller;

import com.plq.grammarly.model.entity.ExchangeCode;
import com.plq.grammarly.model.vo.ExchangeCodeQueryVO;
import com.plq.grammarly.model.vo.ExchangeParamVO;
import com.plq.grammarly.model.vo.GenParamVO;
import com.plq.grammarly.model.vo.QuestionExchangeCodeQueryVO;
import com.plq.grammarly.service.ExchangeCodeService;
import com.plq.grammarly.service.QuestionExchangeCodeService;
import com.plq.grammarly.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 问题码兑换
 *
 * @author luquan.peng
 * @date 2021/06/03
 */
@Controller
public class QuestionExchangeCodeController {

    private final QuestionExchangeCodeService questionExchangeCodeService;

    public QuestionExchangeCodeController(QuestionExchangeCodeService questionExchangeCodeService) {
        this.questionExchangeCodeService = questionExchangeCodeService;
    }

    @GetMapping("/questionExchange")
    public String index() {
        return "questionExchange";
    }

    @GetMapping("/questionExchangeCode")
    public String questionExchangeCode() {
        return "questionExchangeCode";
    }

    @GetMapping("/questionExchangeCodes")
    @ResponseBody
    public Map<String, Object> pageQuery(QuestionExchangeCodeQueryVO questionExchangeCodeQueryVO) {
        return questionExchangeCodeService.pageQuery(questionExchangeCodeQueryVO);
    }


}