package com.vlxtrcore;

import com.vlxtrcore.config.GoogleDriveConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ImportAutoConfiguration(exclude = GoogleDriveConfig.class)
class VlxtrCoreApplicationTests {

	@Test
	void contextLoads() {
	}

}
