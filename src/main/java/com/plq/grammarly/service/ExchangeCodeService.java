package com.plq.grammarly.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.plq.grammarly.model.entity.ExchangeCode;
import com.plq.grammarly.model.entity.GrammarlyAccount;
import com.plq.grammarly.model.vo.ExchangeCodeQueryVO;
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

    /**
     * 移除
     * @param exchangeCode
     * @return
     */
    boolean remove(ExchangeCode exchangeCode);

    List<ExchangeCode> listMemberExpire(Date now);

    Map<String, Object> pageQuery(ExchangeCodeQueryVO exchangeCodeQueryVO);

    ExchangeCode getObjById(String id);

    void updateObj(ExchangeCode exchangeCode);

    void delete(ExchangeCode exchangeCode);

    boolean removeMemberOnGrammarly(ExchangeCode exchangeCode, GrammarlyAccount grammarlyAccount);
}
