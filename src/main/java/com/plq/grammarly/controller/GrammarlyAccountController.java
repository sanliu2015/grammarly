package com.plq.grammarly.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.plq.grammarly.model.entity.GrammarlyAccount;
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

import java.util.List;
import java.util.Map;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/07
 */
@Controller
public class GrammarlyAccountController {

    private final GrammarlyAccountService grammarlyAccountService;

    public GrammarlyAccountController(GrammarlyAccountService grammarlyAccountService) {
        this.grammarlyAccountService = grammarlyAccountService;
    }

    @GetMapping("/grammarlyAccounts")
    public String listAccount(Map<String, Object> map) {
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

    @PostMapping("/grammarlyAccount")
    @ResponseBody
    public Result saveAccount(@RequestBody @Validated GrammarlyAccount grammarlyAccount) {
        grammarlyAccountService.save(grammarlyAccount);
        return Result.success();
    }

    @GetMapping("/grammarlyAccount/{id}")
    @ResponseBody
    public Result getAccount(@PathVariable String id) {
        GrammarlyAccount grammarlyAccount = grammarlyAccountService.findById(id);
        return Result.success(grammarlyAccount);
    }

}
