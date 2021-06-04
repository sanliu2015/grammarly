package com.plq.grammarly.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.plq.grammarly.model.entity.ExchangeCode;
import com.plq.grammarly.model.entity.GrammarlyAccount;
import com.plq.grammarly.model.vo.GenParamVO;
import com.plq.grammarly.repository.ExchangeCodeRepository;
import com.plq.grammarly.service.ExchangeCodeService;
import com.plq.grammarly.service.GrammarlyAccountService;
import com.plq.grammarly.util.Result;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/03
 */
@Service
public class ExchangeCodeServiceImpl implements ExchangeCodeService {

    private final ExchangeCodeRepository exchangeCodeRepository;
    private final GrammarlyAccountService grammarlyAccountService;

    public ExchangeCodeServiceImpl(ExchangeCodeRepository exchangeCodeRepository, GrammarlyAccountService grammarlyAccountService) {
        this.exchangeCodeRepository = exchangeCodeRepository;
        this.grammarlyAccountService = grammarlyAccountService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Set<String> gen(GenParamVO genParamVO) {
        Set<String> numbers = new HashSet<>(genParamVO.getCount());
        Set<ExchangeCode> exchangeCodes = new HashSet<>();
        int genCount = 0;
        while (genCount < genParamVO.getCount()) {
            String number = RandomUtil.randomString(16);
            if (!numbers.contains(number)) {
                ExchangeCode exchangeCode = ExchangeCode.builder()
                        .number(number).validDays(genParamVO.getValidDays())
                        .exchangeDeadline(genParamVO.getExchangeDeadline())
                        .build();
                exchangeCodes.add(exchangeCode);
                genCount ++;
            }
        }
        return numbers;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result exchange(String number) {
        Example<ExchangeCode> example = Example.of(ExchangeCode.builder().number(number).build());
        ExchangeCode exchangeCode = exchangeCodeRepository.findOne(example).get();
        if (exchangeCode == null) {
            return Result.failure("不存在此兑换码");
        } else {
            if (exchangeCode.getExchangeStatus()) {
                return Result.failure("此兑换码已经被兑换");
            }
            if (exchangeCode.getExchangeDeadline() != null) {
                if (DateUtil.compare(exchangeCode.getExchangeDeadline(), new Date()) > 0) {
                    return Result.failure("此兑换码已经过了截止兑换日期：" + DateUtil.format(exchangeCode.getExchangeDeadline(), "yyyyMMdd"));
                }
            }
            addMember(exchangeCode);
        }
        return Result.success();
    }

    /**
     * 邀请成员
     * @param exchangeCode
     */
    private void addMember(ExchangeCode exchangeCode) {
        String accountType = exchangeCode.getValidDays() < 30 ? "0" : "1";
        List<GrammarlyAccount> accounts = grammarlyAccountService.listByAccountType(accountType);

    }
}
