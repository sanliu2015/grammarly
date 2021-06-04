package com.plq.grammarly.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.plq.grammarly.model.entity.ExchangeCode;
import com.plq.grammarly.model.vo.GenParamVO;
import com.plq.grammarly.repository.ExchangeCodeRepository;
import com.plq.grammarly.service.ExchangeCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/03
 */
@Service
public class ExchangeCodeServiceImpl implements ExchangeCodeService {

    private final ExchangeCodeRepository exchangeCodeRepository;

    public ExchangeCodeServiceImpl(ExchangeCodeRepository exchangeCodeRepository) {
        this.exchangeCodeRepository = exchangeCodeRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> gen(GenParamVO genParamVO) {
        List<String> numbers = new ArrayList<>(genParamVO.getCount());
        for (int i=0; i< genParamVO.getCount(); i++) {
            ExchangeCode exchangeCode = new ExchangeCode();
            exchangeCode.setNumber(RandomUtil.randomString(16));
            exchangeCode.setValidDays(genParamVO.getValidDays());
            exchangeCode.setExchangeDeadline(genParamVO.getExchangeDeadline());
            exchangeCodeRepository.insert(exchangeCode);
            numbers.add(exchangeCode.getNumber());
        }
        return numbers;
    }
}
