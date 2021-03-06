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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        Set<ExchangeCode> exchangeCodes = new HashSet<>();
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
                    log.warn("????????????????????????{}????????????????????????", number);
                }
            }
        }
        return numbers;
    }


    @Override
//    @Transactional(rollbackFor = Exception.class)
    public Result exchange(ExchangeParamVO exchangeParamVO) {
        Example<ExchangeCode> example = Example.of(ExchangeCode.builder().number(exchangeParamVO.getNumber()).build());
        ExchangeCode exchangeCode = exchangeCodeRepository.findOne(example).orElse(null);
        if (exchangeCode == null) {
            return Result.failure("?????????????????????");
        } else {
            if (exchangeCode.getExchangeStatus()) {
                return Result.failure("???????????????????????????");
            }
            if (exchangeCode.getExchangeDeadline() != null) {
                if (DateUtil.compare(exchangeCode.getExchangeDeadline(), new Date()) < 0) {
                    return Result.failure("??????????????????????????????????????????" + DateUtil.format(exchangeCode.getExchangeDeadline(), "yyyyMMdd"));
                }
            }
            exchangeCode.setEmail(exchangeParamVO.getEmail());
            boolean result = addMember(exchangeCode);
            if (result) {
                return Result.success("???????????????????????????????????????????????????");
            } else {
                return Result.failure("???????????????????????????????????????????????????");
            }
        }
    }

    /**
     * ????????????
     * @param exchangeCode ????????????
     */
    private boolean addMember(ExchangeCode exchangeCode) {
        String accountType = exchangeCode.getValidDays() < 30 ? "0" : "1";
        List<GrammarlyAccount> accounts = grammarlyAccountService.listByAccountType(accountType);
        // ????????????????????????????????????????????????????????????????????????
        if (accounts.size() == 0) {
            log.error("????????????????????????{}?????????????????????grammarly??????????????????????????????????????????", exchangeCode.getValidDays());
            accounts = grammarlyAccountService.listAll();
        }
//        Collections.shuffle(accounts);    // ????????????
        // ??????????????????
        Collections.sort(accounts, (o1, o2) -> {
            int i = o2.getCreateTime().compareTo(o1.getCreateTime());
            if (i == 0) {
                return o2.getId().compareTo(o1.getId());
            }
            return i;
        });
        boolean successFlag = false;
        log.info("??????????????????{},????????????:{},??????grammarly?????????:{}", exchangeCode.getNumber(), exchangeCode.getEmail(), accounts);
        if (accounts.size() > 0) {
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
                   } else {
                       StringBuilder sb = new StringBuilder(exchangeCode.getErrorMsg() == null ? "" : exchangeCode.getErrorMsg());
                       sb.append("grammarly?????????").append(grammarlyAccount.getAccount())
                               .append("????????????????????????:").append(httpResponse.getStatus())
                               .append("|");
                       log.info("grammarly?????????{}?????????{}?????????????????????{}???????????????{}", grammarlyAccount.getAccount(),
                               exchangeCode.getEmail(), httpResponse.getStatus(), httpResponse.body());
                       exchangeCode.setErrorMsg(sb.toString());
                   }
                   exchangeCodeRepository.save(exchangeCode);
                } catch (Exception e) {
                    log.error("grammarly????????????????????????", e);
                }
            }
        } else {
            log.error("??????grammarly?????????????????????0");
        }

        if (!successFlag) {
            log.error("??????{}??????{}??????", exchangeCode.getEmail(), exchangeCode.getNumber());
        }
        log.info("??????????????????{},??????{}", exchangeCode.getNumber(), successFlag);
        return successFlag;
    }

    @Override
    public boolean remove(ExchangeCode exchangeCode) {
        GrammarlyAccount grammarlyAccount = grammarlyAccountService.findByAccount(exchangeCode.getInviterAccount());
        if (grammarlyAccount == null) {
            log.warn("????????????????????????{}????????????????????????{}?????????????????????", exchangeCode.getNumber(), exchangeCode.getInviterAccount());
            return false;
        }
        return SpringUtil.getBean(ExchangeCodeService.class).removeMemberOnGrammarly(exchangeCode, grammarlyAccount);
    }

    @Override
    public List<ExchangeCode> listMemberExpire(Date date) {
        return exchangeCodeRepository.findByExchangeStatusTrueAndRemoveStatusFalseAndMemberDeadlineLessThan(date);
    }

    @Override
    public List<ExchangeCode> findByExchangeStatusFalseAndExchangeDeadlineBetween(Date sdate, Date edate) {
        return exchangeCodeRepository.findByExchangeStatusFalseAndExchangeDeadlineBetween(sdate, edate);
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
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains());
//                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains()); // ????????????
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
                log.info("grammarly?????????????????????exchangeCode???{}", exchangeCode);
            } else {
                StringBuilder sb = new StringBuilder(exchangeCode.getErrorMsg() == null ? "" : exchangeCode.getErrorMsg());
                sb.append("grammarly?????????").append(grammarlyAccount.getAccount())
                        .append("????????????????????????:").append(httpResponse.getStatus())
                        .append("|");
                log.error("grammarly?????????{}?????????{}?????????????????????{}???????????????{}", grammarlyAccount.getAccount(),
                        exchangeCode.getEmail(), httpResponse.getStatus(), httpResponse.body());
                exchangeCode.setErrorMsg(sb.toString());
            }
            exchangeCodeRepository.save(exchangeCode);
        } catch (Exception e) {
            log.error("grammarly????????????????????????", e);
        }
        return flag;
    }
}
