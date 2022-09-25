package com.plq.grammarly.service;

import com.plq.grammarly.model.entity.QuestionExchangeCode;
import com.plq.grammarly.model.vo.QuestionExchangeCodeQueryVO;

import java.util.List;
import java.util.Map;

public interface QuestionExchangeCodeService {
    Map<String, Object> pageQuery(QuestionExchangeCodeQueryVO questionExchangeCodeQueryVO);

    /**
     * 批量更新
     * @param day
     */
    void batchUpdateExpire(String day);

    void updateObj(QuestionExchangeCode questionExchangeCode);

    List<QuestionExchangeCode> findByDeadlineLessThanAndStatus(String deadline, String status);
}
