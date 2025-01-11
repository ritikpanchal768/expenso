package com.example.expenso;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ExpensoApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("Database URL: " + System.getenv("DATASOURCE_URL"));
		System.out.println("Database User: " + System.getenv("DATASOURCE_USER"));
	}

}
