package com.plq.grammarly.repository;

import com.plq.grammarly.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/16
 */
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
