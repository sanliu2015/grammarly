package com.plq.grammarly.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.plq.grammarly.model.entity.ExchangeCode;
import com.plq.grammarly.model.entity.GrammarlyAccount;
import com.plq.grammarly.model.vo.ExchangeCodeQueryVO;
import com.plq.grammarly.model.vo.ExchangeParamVO;
import com.plq.grammarly.model.vo.GenParamVO;
import com.plq.grammarly.repository.ExchangeCodeRepository;
import com.plq.grammarly.service.ExchangeCodeService;
import com.plq.grammarly.service.GrammarlyAccountService;
import com.plq.grammarly.util.BizUtil;
import com.plq.grammarly.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/03
 */
@Slf4j
@Service
public class ExchangeCodeServiceImpl implements ExchangeCodeService {

    private final ExchangeCodeRepository exchangeCodeRepository;
    private final GrammarlyAccountService grammarlyAccountService;

    public ExchangeCodeServiceImpl(ExchangeCodeRepository exchangeCodeRepository, GrammarlyAccountService grammarlyAccountService) {
        this.exchangeCodeRepository = exchangeCodeRepository;
        this.grammarlyAccountService = grammarlyAccountService;
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public Set<String> gen(GenParamVO genParamVO) {
        Set<String> numbers = new HashSet<>(genParamVO.getCount());
        int genCount = 0;
        while (genCount < genParamVO.getCount()) {
            String number = RandomUtil.randomString(16);
            if (!numbers.contains(number)) {
                ExchangeCode exchangeCode = ExchangeCode.builder()
                        .number(number).validDays(genParamVO.getValidDays())
                        .exchangeDeadline(genParamVO.getExchangeDeadline())
                        .exchangeStatus(false)
                        .expireStatus(false)
                        .removeStatus(false)
                        .build();
                try {
                    exchangeCodeRepository.save(exchangeCode);
                    numbers.add(number);
                    genCount ++;
                } catch (Exception e) {
                    log.warn("生成的兑换码重复{}，丢弃，继续重生", number);
                }
            }
        }
        return numbers;
    }


    @Override
//    @Transactional(rollbackFor = Exception.class)
    public Result exchange(ExchangeParamVO exchangeParamVO) {
        log.info("开始兑换：{}", exchangeParamVO.getNumber());
        Example<ExchangeCode> example = Example.of(ExchangeCode.builder().number(exchangeParamVO.getNumber()).build());
        ExchangeCode exchangeCode = exchangeCodeRepository.findOne(example).orElse(null);
        if (exchangeCode == null) {
            return Result.failure("不存在此兑换码");
        } else {
            if (exchangeCode.getExchangeStatus()) {
                return Result.failure("此兑换码已经被兑换");
            }
            if (exchangeCode.getExchangeDeadline() != null) {
                if (DateUtil.compare(exchangeCode.getExchangeDeadline(), new Date()) < 0) {
                    return Result.failure("此兑换码已超过兑换截止日期：" + DateUtil.format(exchangeCode.getExchangeDeadline(), "yyyyMMdd"));
                }
            }
            exchangeCode.setEmail(exchangeParamVO.getEmail());
            boolean result = addMember(exchangeCode);
            if (result) {
                return Result.success("兑换成功，请前往您的邮箱进行查收！");
            } else {
                return Result.failure("兑换过程出错，请联系客服！");
            }
        }
    }

    /**
     * 邀请成员
     * @param exchangeCode 兑换实体
     */
    private boolean addMember(ExchangeCode exchangeCode) {
        String accountType = exchangeCode.getValidDays() < 30 ? "0" : "1";
        List<GrammarlyAccount> accounts = grammarlyAccountService.listByAccountType(accountType);
        // 如果优先根据会员天数对应分类没找到，采取查找所有
        if (accounts.size() == 0) {
            log.error("优先根据会员天数{}对应分类没找到grammarly账号配置，故查找所有分类配置", exchangeCode.getValidDays());
            accounts = grammarlyAccountService.listAll();
        }
//        Collections.shuffle(accounts);    // 随机排序
        // 创建时间降序
        Collections.sort(accounts, (o1, o2) -> {
            int i = o2.getCreateTime().compareTo(o1.getCreateTime());
            if (i == 0) {
                return o2.getId().compareTo(o1.getId());
            }
            return i;
        });
        boolean successFlag = false;
        if (accounts.size() > 0) {
            log.info("用户开始兑换{},所填邮箱:{},候选grammarly账号数量:{}", exchangeCode.getNumber(), exchangeCode.getEmail(), accounts.size());
            List<Map<String, Object>> dataList = new ArrayList<>();
            Map<String, Object> map = new HashMap<>(16);
            map.put("email", exchangeCode.getEmail());
            map.put("firstName", "");
            map.put("secondName", "");
            map.put("ineligibleEmail", false);
            map.put("assignedToOtherInstitutionEmail", false);
            dataList.add(map);
            String body = JSONUtil.toJsonStr(dataList);
            for (GrammarlyAccount grammarlyAccount : accounts) {
                Map<String, String> httpRequestHeadMap = BizUtil.convertFromCurl(grammarlyAccount.getCurlStr());
                HttpRequest httpRequest = BizUtil.buildInviteHttpRequest(httpRequestHeadMap);
                httpRequest.body(body);
                try {
                   HttpResponse httpResponse = httpRequest.timeout(30000).execute();
                   if (httpResponse.getStatus() == HttpStatus.HTTP_OK) {
                       successFlag = true;
                       exchangeCode.setInviterAccount(grammarlyAccount.getAccount());
                       exchangeCode.setExchangeTime(new Date());
                       exchangeCode.setExchangeStatus(true);
                       exchangeCode.setMemberDeadline(DateUtil.offsetDay(exchangeCode.getExchangeTime(), exchangeCode.getValidDays()));
                       exchangeCode.setErrorMsg("");
                       log.info("grammarly账号：{}，邀请{}成功，响应码：{}，响应体：{}", grammarlyAccount.getAccount(),
                               exchangeCode.getEmail(), httpResponse.getStatus(), httpResponse.body());
                       break;
                   } else {
                       StringBuilder sb = new StringBuilder(exchangeCode.getErrorMsg() == null ? "" : exchangeCode.getErrorMsg());
                       sb.append("grammarly账号：").append(grammarlyAccount.getAccount())
                               .append("邀请失败，响应码:").append(httpResponse.getStatus())
                               .append("|");
                       log.info("grammarly账号：{}，邀请{}失败，响应码：{}，响应体：{}", grammarlyAccount.getAccount(),
                               exchangeCode.getEmail(), httpResponse.getStatus(), httpResponse.body());
                       exchangeCode.setErrorMsg(sb.toString());
                   }

                } catch (Exception e) {
                    log.error("grammarly邀请用户网络异常", e);
                }
            }
        } else {
            log.error("可用grammarly账号配置总数为0");
        }
        exchangeCodeRepository.save(exchangeCode);
        if (!successFlag) {
            log.error("用户{}兑换{}失败", exchangeCode.getEmail(), exchangeCode.getNumber());
        }
        log.info("用户结束兑换{},结果{}", exchangeCode.getNumber(), successFlag);
        return successFlag;
    }

    @Override
    public boolean remove(ExchangeCode exchangeCode) {
        GrammarlyAccount grammarlyAccount = grammarlyAccountService.findByAccount(exchangeCode.getInviterAccount());
        if (grammarlyAccount == null) {
            log.warn("移除失败，兑换码{}对应的邀请方账号{}详细信息没找到", exchangeCode.getNumber(), exchangeCode.getInviterAccount());
            exchangeCode.setErrorMsg("对应的邀请方账号信息没找到");
            exchangeCodeRepository.save(exchangeCode);
            return false;
        }
        return SpringUtil.getBean(ExchangeCodeService.class).removeMemberOnGrammarly(exchangeCode, grammarlyAccount);
    }

    @Override
    public List<ExchangeCode> listMemberExpire(Date date) {
        return exchangeCodeRepository.findByExchangeStatusTrueAndExpireStatusFalseAndRemoveStatusFalseAndMemberDeadlineLessThan(date);
    }

    @Override
    public List<ExchangeCode> findByExchangeStatusFalseAndExchangeExpireStatusFalseAndExchangeDeadlineBetween(Date sdate, Date edate) {
        return exchangeCodeRepository.findByExchangeStatusFalseAndExchangeExpireStatusFalseAndExchangeDeadlineBetween(sdate, edate);
    }

    @Override
    public Map<String, Object> pageQuery(ExchangeCodeQueryVO exchangeCodeQueryVO) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(exchangeCodeQueryVO.getPage() - 1, exchangeCodeQueryVO.getLimit(), sort);
        ExchangeCode exchangeCode = new ExchangeCode();
        BeanUtil.copyProperties(exchangeCodeQueryVO, exchangeCode);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withMatcher("number", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("inviterAccount", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains());
//                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains()); // 模糊搜索
//        if (exchangeCodeQueryVO.getMemberDeadlineStart() != null) {
//            matcher.withMatcher("memberDeadline", ExampleMatcher.)
//        }

        if (exchangeCodeQueryVO.getCond2() != null && exchangeCodeQueryVO.getCond2()) {
            exchangeCode.setExpireStatus(true);
            exchangeCode.setRemoveStatus(false);
            matcher.withMatcher("expireStatus", ExampleMatcher.GenericPropertyMatchers.exact());
            matcher.withMatcher("removeStatus", ExampleMatcher.GenericPropertyMatchers.exact());
        }
        if (exchangeCodeQueryVO.getCond1() != null && exchangeCodeQueryVO.getCond1()) {
            exchangeCode.setExchangeStatus(false);
            exchangeCode.setExchangeExpireStatus(true);
            matcher.withMatcher("exchangeStatus", ExampleMatcher.GenericPropertyMatchers.exact());
            matcher.withMatcher("exchangeExpireStatus", ExampleMatcher.GenericPropertyMatchers.exact());
        }
        Example example = Example.of(exchangeCode, matcher);
        Page page = exchangeCodeRepository.findAll(example, pageRequest);
        Map<String, Object> result = new HashMap<>();
        result.put("data", page.getContent());
        result.put("count", page.getTotalElements());
        result.put("code", 0);
        result.put("msg", "");
        return result;
    }

    @Override
    public ExchangeCode getObjById(String id) {
        return exchangeCodeRepository.findById(id).orElse(null);
    }

    @Override
    public void updateObj(ExchangeCode exchangeCode) {
        exchangeCodeRepository.save(exchangeCode);
    }

    @Override
    public void delete(ExchangeCode exchangeCode) {
        exchangeCodeRepository.delete(exchangeCode);
    }

    @Override
    public boolean removeMemberOnGrammarly(ExchangeCode exchangeCode, GrammarlyAccount grammarlyAccount) {
        boolean flag = false;
        Map<String, String> httpRequestHeadMap = BizUtil.convertFromCurl(grammarlyAccount.getCurlStr());
        HttpRequest httpRequest = BizUtil.buildRemoveHttpRequest(httpRequestHeadMap);
        Map<String, Object> map = new HashMap<>(16);
        map.put("type", "Active");
        map.put("reverse", false);
        map.put("emails", Lists.newArrayList(exchangeCode.getEmail()));
        httpRequest.body(JSONUtil.toJsonStr(map));
        try {
            HttpResponse httpResponse = httpRequest.timeout(30000).execute();
            if (httpResponse.getStatus() == HttpStatus.HTTP_NO_CONTENT) {
                exchangeCode.setRemoveStatus(true);
                exchangeCode.setRemoveTime(new Date());
                exchangeCode.setErrorMsg("");
                flag = true;
                log.info("grammarly删除用户成功，exchangeCode：{}", exchangeCode);
            } else {
                StringBuilder sb = new StringBuilder(exchangeCode.getErrorMsg() == null ? "" : exchangeCode.getErrorMsg());
                sb.append("grammarly账号：").append(grammarlyAccount.getAccount())
                        .append("删除失败，响应码:").append(httpResponse.getStatus())
                        .append("|");
                log.error("grammarly账号：{}，删除{}失败，响应码：{}，响应体：{}", grammarlyAccount.getAccount(),
                        exchangeCode.getEmail(), httpResponse.getStatus(), httpResponse.body());
                exchangeCode.setErrorMsg(sb.toString());
            }
            exchangeCodeRepository.save(exchangeCode);
        } catch (Exception e) {
            log.error("grammarly删除用户网络异常", e);
        }
        return flag;
    }
}
