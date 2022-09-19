package com.plq.grammarly.repository;

import com.plq.grammarly.model.entity.QuestionExchangeCode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionExchangeCodeRepository extends MongoRepository<QuestionExchangeCode, String> {
}
