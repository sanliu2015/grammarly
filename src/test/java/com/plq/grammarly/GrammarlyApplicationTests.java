package com.plq.grammarly;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.plq.grammarly.model.entity.ExchangeCode;
import com.plq.grammarly.model.vo.GenParamVO;
import com.plq.grammarly.repository.ExchangeCodeRepository;
import com.plq.grammarly.service.ExchangeCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Profile("dev")
@SpringBootTest
class GrammarlyApplicationTests {

	@Autowired
	private ExchangeCodeRepository exchangeCodeRepository;
	@Autowired
	private ExchangeCodeService exchangeCodeService;

	@Test
	void contextLoads() {
	}

	@Test
	void insertDataToMongo() {
		ExchangeCode exchangeCode = new ExchangeCode();
		exchangeCode.setNumber(RandomUtil.randomString(10));
		exchangeCodeRepository.insert(exchangeCode);
	}

	@Test
	void genTest() {
		GenParamVO genParamVO = new GenParamVO();
		genParamVO.setCount(5);
		genParamVO.setValidDays(180);
		genParamVO.setExchangeDeadline(DateUtil.parse("2021-07-01", "yyyy-MM-dd"));
		List<String> numbers = exchangeCodeService.gen(genParamVO);
		System.out.println(JSONUtil.toJsonStr(numbers));
	}

}
