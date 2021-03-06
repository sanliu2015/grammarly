package com.plq.grammarly.service.impl;

import com.plq.grammarly.model.entity.GrammarlyAccount;
import com.plq.grammarly.repository.GrammarlyAccountRepository;
import com.plq.grammarly.service.GrammarlyAccountService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public List<GrammarlyAccount> listAll() {
        return grammarlyAccountRepository.findAll();
    }

    @Override
    public GrammarlyAccount findByAccount(String account) {
        GrammarlyAccount grammarlyAccount = new GrammarlyAccount();
        grammarlyAccount.setAccount(account);
        Example<GrammarlyAccount> example = Example.of(grammarlyAccount);
        return grammarlyAccountRepository.findOne(example).orElse(null);
    }

    @Override
    public void save(GrammarlyAccount grammarlyAccount) {
        grammarlyAccountRepository.save(grammarlyAccount);
    }

    @Override
    public GrammarlyAccount findById(String id) {
        return grammarlyAccountRepository.findById(id).orElse(new GrammarlyAccount());
    }

    @Override
    public void deleteById(String id) {
        grammarlyAccountRepository.deleteById(id);
    }
}
