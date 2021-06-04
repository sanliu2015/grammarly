package com.plq.grammarly.service.impl;

import com.plq.grammarly.model.entity.ExchangeCode;
import com.plq.grammarly.model.entity.GrammarlyAccount;
import com.plq.grammarly.repository.GrammarlyAccountRepository;
import com.plq.grammarly.service.GrammarlyAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/04
 */
@Service
public class GrammarlyAccountServiceImpl implements GrammarlyAccountService {

    private final GrammarlyAccountRepository grammarlyAccountRepository;

    public GrammarlyAccountServiceImpl(GrammarlyAccountRepository grammarlyAccountRepository) {
        this.grammarlyAccountRepository = grammarlyAccountRepository;
    }

    @Override
    public List<GrammarlyAccount> listByAccountType(String accountType) {
        GrammarlyAccount grammarlyAccount = new GrammarlyAccount();
        grammarlyAccount.setAccountType(accountType);
        Example<GrammarlyAccount> example = Example.of(grammarlyAccount);
        return grammarlyAccountRepository.findAll(example);
    }
}
