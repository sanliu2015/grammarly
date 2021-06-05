package com.plq.grammarly.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.plq.grammarly.model.entity.GrammarlyAccount;
import com.plq.grammarly.model.vo.ExchangeParamVO;
import com.plq.grammarly.model.vo.GenParamVO;
import com.plq.grammarly.service.ExchangeCodeService;
import com.plq.grammarly.service.GrammarlyAccountService;
import com.plq.grammarly.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final GrammarlyAccountService grammarlyAccountService;

    public ExchangeCodeController(ExchangeCodeService exchangeCodeService, GrammarlyAccountService grammarlyAccountService) {
        this.exchangeCodeService = exchangeCodeService;
        this.grammarlyAccountService = grammarlyAccountService;
    }

    @GetMapping(value = { "index", "" } )
    public String index() {
        return "index";
    }

    @GetMapping("/grammarly/exchangeCode/gen")
    public String genMng() {
        return "gen";
    }

    @GetMapping("/grammarly/accounts")
    public String listAccounts(Map<String, Object> map) {
        List<GrammarlyAccount> grammarlyAccounts = grammarlyAccountService.listAll();
        for (GrammarlyAccount grammarlyAccount : grammarlyAccounts) {
            if ("1".equals(grammarlyAccount.getAccountType())) {
                grammarlyAccount.setTypeName("适用30天及以上");
            } else {
                grammarlyAccount.setTypeName("适用30天以下");
            }
            if (StrUtil.isEmpty(grammarlyAccount.getCurlStr())) {
                grammarlyAccount.setCurlIsSet("未设置");
            } else {
                grammarlyAccount.setCurlIsSet("已设置");
            }
        }
        map.put("accounts", grammarlyAccounts);
        map.put("accountsStr", JSONUtil.toJsonStr(grammarlyAccounts));
        return "account";
    }

    /**
     * 生成兑换码
     * @param genParamVO
     * @return
     */
    @PostMapping("/grammarly/exchangeCode/gen")
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
    @PostMapping("/grammarly/exchangeCode/exchange")
    @ResponseBody
    public Result exchange(@RequestBody @Validated ExchangeParamVO exchangeParamVO) {
        return exchangeCodeService.exchange(exchangeParamVO);
    }

    @PostMapping("/grammarly/grammarlyAccount")
    @ResponseBody
    public Result saveAccount(@RequestBody @Validated GrammarlyAccount grammarlyAccount) {
        grammarlyAccountService.save(grammarlyAccount);
        return Result.success();
    }

    @GetMapping("/grammarly/grammarlyAccount/{id}")
    @ResponseBody
    public Result getAccount(@PathVariable String id) {
        GrammarlyAccount grammarlyAccount = grammarlyAccountService.findById(id);
        return Result.success(grammarlyAccount);
    }

}
