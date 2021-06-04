package com.plq.grammarly.service;

import java.util.Set;

import com.plq.grammarly.model.vo.ExchangeParamVO;
import com.plq.grammarly.model.vo.GenParamVO;
import com.plq.grammarly.util.Result;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/03
 */
public interface ExchangeCodeService {

    /**
     * 批量生产兑换码
     * @param genParamVO
     * @return
     */
    Set<String> gen(GenParamVO genParamVO);

    /**
     * 兑换
     * @param exchangeParamVO
     * @return
     */
    Result exchange(ExchangeParamVO exchangeParamVO);
}
