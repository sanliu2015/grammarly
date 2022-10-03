package com.plq.grammarly.repository;

import com.plq.grammarly.model.entity.QuestionExchangeCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionExchangeCodeRepository extends MongoRepository<QuestionExchangeCode, String> {
    List<QuestionExchangeCode> findByDeadlineLessThanAndStatus(String day, String status);

    QuestionExchangeCode findByCode(String code);
}
