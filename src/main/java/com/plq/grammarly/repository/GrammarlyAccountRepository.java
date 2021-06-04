package com.plq.grammarly.repository;

import com.plq.grammarly.model.entity.GrammarlyAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/04
 */
public interface GrammarlyAccountRepository extends MongoRepository<GrammarlyAccount, String> {
}
