package com.plq.grammarly.service;

import com.plq.grammarly.model.vo.QuestionExchangeCodeQueryVO;

import java.util.Map;

public interface QuestionExchangeCodeService {
    Map<String, Object> pageQuery(QuestionExchangeCodeQueryVO questionExchangeCodeQueryVO);
}
