package com.plq.grammarly.service;

import java.util.List;

import com.plq.grammarly.model.vo.GenParamVO;

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
    List<String> gen(GenParamVO genParamVO);
}
