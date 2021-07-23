package com.plq.grammarly.repository;

import com.plq.grammarly.model.entity.EmailConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/07/23
 */
public interface EmailConfigRepository extends MongoRepository<EmailConfig, String> {
}
