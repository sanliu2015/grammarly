package com.plq.grammarly.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.plq.grammarly.model.entity.QuestionExchangeCode;
import com.plq.grammarly.model.vo.QuestionExchangeCodeQueryVO;
import com.plq.grammarly.repository.QuestionExchangeCodeRepository;
import com.plq.grammarly.service.QuestionExchangeCodeService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QuestionExchangeCodeServiceImpl implements QuestionExchangeCodeService {


    private final QuestionExchangeCodeRepository questionExchangeCodeRepository;

    public QuestionExchangeCodeServiceImpl(QuestionExchangeCodeRepository questionExchangeCodeRepository) {
        this.questionExchangeCodeRepository = questionExchangeCodeRepository;
    }

    @Override
    public Map<String, Object> pageQuery(QuestionExchangeCodeQueryVO questionExchangeCodeQueryVO) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(questionExchangeCodeQueryVO.getPage() - 1, questionExchangeCodeQueryVO.getLimit(), sort);
        QuestionExchangeCode questionExchangeCode = new QuestionExchangeCode();
        BeanUtil.copyProperties(questionExchangeCodeQueryVO, questionExchangeCode);
        ExampleMatcher matcher = ExampleMatcher.matching()
                // python插入的没有_class属性
                .withIgnorePaths("_class")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withMatcher("code", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains());
        Example example = Example.of(questionExchangeCode, matcher);
        Page page = questionExchangeCodeRepository.findAll(example, pageRequest);
        Map<String, Object> result = new HashMap<>();
        result.put("data", page.getContent());
        result.put("count", page.getTotalElements());
        result.put("code", 0);
        result.put("msg", "");
        return result;
    }
}
