package com.plq.grammarly;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest
class GrammarlyApplicationTests {

	@Test
	@Scheduled(fixedRate = 1000L)
	void contextLoads() {
		System.out.println(LocalTime.now());
	}

}
