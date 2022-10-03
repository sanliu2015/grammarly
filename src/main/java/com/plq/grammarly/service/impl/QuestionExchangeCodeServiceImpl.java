package com.plq.grammarly.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.google.common.collect.Lists;
import com.plq.grammarly.email.MailRequest;
import com.plq.grammarly.email.SendMailService;
import com.plq.grammarly.model.entity.ExchangeCode;
import com.plq.grammarly.model.entity.QuestionExchangeCode;
import com.plq.grammarly.model.vo.QuestionExchangeCodeGenParamVO;
import com.plq.grammarly.model.vo.QuestionExchangeCodeQueryVO;
import com.plq.grammarly.model.vo.QuestionExchangeParamVO;
import com.plq.grammarly.repository.QuestionExchangeCodeRepository;
import com.plq.grammarly.selenium.SeleniumService;
import com.plq.grammarly.service.QuestionExchangeCodeService;
import com.plq.grammarly.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class QuestionExchangeCodeServiceImpl implements QuestionExchangeCodeService {


    private final QuestionExchangeCodeRepository questionExchangeCodeRepository;

    private final SeleniumService seleniumService;

    private final SendMailService sendMailService;

    public QuestionExchangeCodeServiceImpl(QuestionExchangeCodeRepository questionExchangeCodeRepository, SeleniumService seleniumService, SendMailService sendMailService) {
        this.questionExchangeCodeRepository = questionExchangeCodeRepository;
        this.seleniumService = seleniumService;
        this.sendMailService = sendMailService;
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

    @Override
    public void batchUpdateExpire(String expireDate) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                // python插入的没有_class属性
                .withIgnorePaths("_class")
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        QuestionExchangeCode questionExchangeCode = new QuestionExchangeCode();
        questionExchangeCode.setStatus("0");
        questionExchangeCode.setDeadline(expireDate);
        Example example = Example.of(questionExchangeCode, matcher);
        List<QuestionExchangeCode> dataList = questionExchangeCodeRepository.findAll(example);
        if (dataList != null) {
            for (QuestionExchangeCode obj : dataList) {
                obj.setStatus("3");
                questionExchangeCodeRepository.save(obj);
            }
        }
    }

    @Override
    public void updateObj(QuestionExchangeCode questionExchangeCode) {
        questionExchangeCodeRepository.save(questionExchangeCode);
    }

    @Override
    public List<QuestionExchangeCode> findByDeadlineLessThanAndStatus(String day, String status) {
        return questionExchangeCodeRepository.findByDeadlineLessThanAndStatus(day, status);
    }

    @Override
    public Result exchange(QuestionExchangeParamVO questionExchangeParamVO) {
        QuestionExchangeCode questionExchangeCode = questionExchangeCodeRepository.findByCode(questionExchangeParamVO.getCode());
        if (questionExchangeCode == null) {
            return Result.failure("不存在此兑换码");
        }
        // 兑换过程正常
        if ("1".equals(questionExchangeCode.getStatus())) {
            return Result.failure("此兑换码已经被兑换，若您未收到邮件，请联系客服");
        }
        // 兑换过程出现异常
        if ("2".equals(questionExchangeCode.getStatus())) {
            return Result.failure("此兑换码已经被兑换，请联系客服");
        }
        // 兑换码过期
        if ("3".equals(questionExchangeCode.getStatus())) {
            return Result.failure("此兑换码已经过期");
        }
        questionExchangeCode.setQuestionUrl(questionExchangeParamVO.getQuestionUrl());
        questionExchangeCode.setReceiveEmail(questionExchangeParamVO.getReceiveEmail());
        JSONObject result = unlockAndSendEmail(questionExchangeCode);
        if (result.getBool("result")) {
            String filePath = result.getStr("filePath");
            File file = new File(filePath);
            return Result.success(file.getName());
        } else {
            return Result.failure("兑换过程出错，请联系客服！");
        }
    }


    private JSONObject unlockAndSendEmail(QuestionExchangeCode questionExchangeCode) {
        JSONObject result = seleniumService.unlockCourseHeroQuestion(questionExchangeCode);
        if (result.getBool("result")) {
            // 心跳传入的code为null
            if (questionExchangeCode.getCode() != null) {
                questionExchangeCode.setStatus("1");
                questionExchangeCodeRepository.save(questionExchangeCode);
                MailRequest mailRequest = MailRequest.builder()
                        .subject("[coursehero]兑换成功")
                        .content("恭喜你，兑换码:" + questionExchangeCode.getCode()
                                + "兑换成功，问题网址:" + questionExchangeCode.getQuestionUrl() + "，答案详见附件！")
                        .filePaths(Lists.newArrayList(result.getStr("filePath")))
                        .sendTo(questionExchangeCode.getReceiveEmail())
                        .bcc("717208317@qq.com").build();
                sendMailService.sendHtmlMail(mailRequest);
            }
        } else {
            if (questionExchangeCode.getCode() != null) {
                questionExchangeCode.setStatus("2");
                questionExchangeCode.setErrmsg(result.getStr("errmsg"));
                questionExchangeCode.setUpdateTime(DateUtil.formatDateTime(new Date()));
                questionExchangeCodeRepository.save(questionExchangeCode);
            }
        }
        return result;
    }

    @Override
    public Set<String> gen(QuestionExchangeCodeGenParamVO genParamVO) {
        Set<String> numbers = new HashSet<>(genParamVO.getCount());
        int genCount = 0;
        while (genCount < genParamVO.getCount()) {
            String number = RandomUtil.randomString(16);
            if (!numbers.contains(number)) {
                QuestionExchangeCode questionExchangeCode = QuestionExchangeCode.builder()
                        .code(number)
                        .status("0")
                        .deadline(genParamVO.getDeadline())
                        .createTime(DateUtil.formatTime(new Date()))
                        .build();
                try {
                    questionExchangeCodeRepository.save(questionExchangeCode);
                    numbers.add(number);
                    genCount ++;
                } catch (Exception e) {
                    log.warn("生成的兑换码重复{}，丢弃，继续重生", number);
                }
            }
        }
        return numbers;
    }
}
