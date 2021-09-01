package com.plq.grammarly.task;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.plq.grammarly.model.entity.ExchangeCode;
import com.plq.grammarly.model.entity.GrammarlyAccount;
import com.plq.grammarly.service.ExchangeCodeService;
import com.plq.grammarly.service.GrammarlyAccountService;
import com.plq.grammarly.util.BizUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/01
 */
@Component
@Slf4j
@Profile("pro")
public class GrammarlyTask {

    private final GrammarlyAccountService grammarlyAccountService;
    private final ExchangeCodeService exchangeCodeService;

    public GrammarlyTask(GrammarlyAccountService grammarlyAccountService, ExchangeCodeService exchangeCodeService) {
        this.grammarlyAccountService = grammarlyAccountService;
        this.exchangeCodeService = exchangeCodeService;
    }

    /**
     * 每20分钟执行
     */
    @Scheduled(fixedRate = 1000L*60*20)
    public void keepHeartbeat() {
        List<GrammarlyAccount> grammarlyAccountList = grammarlyAccountService.listAll();
        for (GrammarlyAccount grammarlyAccount : grammarlyAccountList) {
            try {
                Map<String, String> httpRequestHeadMap = BizUtil.convertFromCurl(grammarlyAccount.getCurlStr());
                HttpRequest httpRequest = BizUtil.buildQueryHttpRequest(httpRequestHeadMap);
                HttpResponse httpResponse = httpRequest.timeout(30000).execute();
                if (httpResponse.getStatus() == HttpStatus.OK.value()) {
                    log.info("心跳成功:{},httpStatus:{},resBody:{}", grammarlyAccount.getAccount(), httpResponse.getStatus(), httpResponse.body());
                } else {
                    log.info("心跳失败:{},httpStatus:{},resBody:{}", grammarlyAccount.getAccount(), httpResponse.getStatus(), httpResponse.body());
                }
            } catch (Exception e) {
                log.error("心跳网络错误：{}, 异常信息：{}", grammarlyAccount.getAccount(), e.getMessage());
            }
        }
        ThreadUtil.safeSleep(RandomUtil.randomLong(1000L*10, 1000L*10*6));
    }

    /**
     * 过期的自动剔除
     * 每天00：00：30执行
     */
    @Scheduled(cron = "30 0 0 * * ?")
    public void removeMember() {
        try {
            Date now = new Date();
            List<ExchangeCode> exchangeCodes = exchangeCodeService.listMemberExpire(now);
            log.info("共找到{}条过期信息", exchangeCodes.size());
            for (ExchangeCode exchangeCode : exchangeCodes) {
                exchangeCodeService.remove(exchangeCode);
            }
        } catch (Exception e) {
            log.error("定时删除过期任务出现异常", e);
        }
    }

}
