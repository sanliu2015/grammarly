package com.plq.grammarly;

import cn.hutool.core.util.RandomUtil;
import com.plq.grammarly.entity.ExchangeCode;
import com.plq.grammarly.repository.ExchangeCodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalTime;

//@Profile("dev")
@SpringBootTest
class GrammarlyApplicationTests {

	@Autowired
	private ExchangeCodeRepository exchangeCodeRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void insertDataToMongo() {
		ExchangeCode exchangeCode = new ExchangeCode();
		exchangeCode.setNumber(RandomUtil.randomString(10));
		exchangeCodeRepository.insert(exchangeCode);
	}

}
