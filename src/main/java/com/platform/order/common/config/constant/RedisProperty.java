package com.platform.order.common.config.constant;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "spring.data.redis.cluster")
public record RedisProperty(List<String> nodes) {
	@ConstructorBinding
	public RedisProperty {
	}
}
