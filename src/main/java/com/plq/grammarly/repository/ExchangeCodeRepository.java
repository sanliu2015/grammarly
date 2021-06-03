package com.plq.grammarly.repository;

import com.plq.grammarly.entity.ExchangeCode;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/03
 */
public interface ExchangeCodeRepository extends MongoRepository<ExchangeCode, String> {
}
