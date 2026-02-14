package com.platform.order.testenv;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.platform.order.config.TestJpaAuditConfig;
import com.platform.order.config.TestQueryDslConfig;

@Import({TestQueryDslConfig.class, TestJpaAuditConfig.class})
@DataJpaTest(showSql = false)
public class RepositoryTest {
}
