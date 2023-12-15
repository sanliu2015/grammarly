package com.plq.grammarly.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.plq.grammarly.model.entity.ExchangeCode;
import com.plq.grammarly.model.entity.GrammarlyAccount;
import com.plq.grammarly.service.ExchangeCodeService;
import com.plq.grammarly.service.GrammarlyAccountService;
import com.plq.grammarly.util.BizUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
                exchangeCode.setExpireStatus(true);  // 标记为会员过期
                exchangeCodeService.remove(exchangeCode);
            }
        } catch (Exception e) {
            log.error("定时删除过期任务出现异常", e);
        }
    }

    /**
     * 兑换截至日期过期状态
     */
    @Scheduled(cron = "15 0 0 * * ?")
    public void exchangeExpire() {
        try {
            Date now = new Date();
            String day = DateUtil.format(now, "yyyy-MM-dd");
            Date sdate = DateUtil.parse(day + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            Date edate = DateUtil.parse(day + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            List<ExchangeCode> exchangeCodes = exchangeCodeService.findByExchangeStatusFalseAndExchangeExpireStatusFalseAndExchangeDeadlineBetween(sdate, edate);
            for (ExchangeCode exchangeCode : exchangeCodes) {
                exchangeCode.setExchangeExpireStatus(true);
                exchangeCodeService.updateObj(exchangeCode);
            }
        } catch (Exception e) {
            log.error("更新兑换过期状态任务出现异常");
        }
    }

    /**
     * 启动把所有未兑换且逾期的兑换过期状态职位true
     */
    @PostConstruct
    public void init() {
        removeMember();
        Date now = new Date();
        String day = DateUtil.format(now, "yyyy-MM-dd");
        Date sdate = DateUtil.parse("2021-06-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date edate = DateUtil.parse(day + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        List<ExchangeCode> exchangeCodes = exchangeCodeService.findByExchangeStatusFalseAndExchangeExpireStatusFalseAndExchangeDeadlineBetween(sdate, edate);
        if (exchangeCodes != null) {
            for (ExchangeCode exchangeCode : exchangeCodes) {
                exchangeCode.setUpdateTime(now);
                exchangeCode.setExchangeExpireStatus(true);
                exchangeCodeService.updateObj(exchangeCode);
            }
        }

    }

}
