package com.plq.grammarly.repository;

import java.util.Date;
import java.util.List;

import com.plq.grammarly.model.entity.ExchangeCode;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/03
 */
public interface ExchangeCodeRepository extends MongoRepository<ExchangeCode, String> {

    List<ExchangeCode> findByExchangeStatusTrueAndExpireStatusFalseAndRemoveStatusFalseAndMemberDeadlineLessThan(Date date);

    List<ExchangeCode> findByExchangeStatusFalseAndExchangeExpireStatusFalseAndExchangeDeadlineBetween(Date sdate, Date edate);
}
