package com.plq.grammarly.service;

import com.plq.grammarly.model.entity.GrammarlyAccount;

import java.util.List;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/04
 */
public interface GrammarlyAccountService {

    List<GrammarlyAccount> listByAccountType(String accountType);

    List<GrammarlyAccount> listAll();

    GrammarlyAccount findByAccount(String inviterAccount);

    void save(GrammarlyAccount grammarlyAccount);

    GrammarlyAccount findById(String id);
}
