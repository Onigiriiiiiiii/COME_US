package com.platform.order.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.platform.order.common.config.constant.RedisProperty;

import lombok.RequiredArgsConstructor;

@Profile({"local", "prod", "container"})
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RedisProperty.class)
public class RedissonConfig {

	private final RedisProperty redisProperty;

	@Value("${spring.data.redis.host:}")
	private String host;

	@Value("${spring.data.redis.port:6379}")
	private int port;

	@Bean(destroyMethod = "shutdown")
	public RedissonClient redissonClient() {
		Config config = new Config();
		if (redisProperty.nodes() != null && !redisProperty.nodes().isEmpty()) {
			var clusterServersConfig = config.useClusterServers();
			redisProperty.nodes().stream()
				.map(this::toRedisAddress)
				.forEach(clusterServersConfig::addNodeAddress);
		} else {
			config.useSingleServer().setAddress(toRedisAddress(host + ":" + port));
		}

		return Redisson.create(config);
	}

	private String toRedisAddress(String node) {
		return node.startsWith("redis://") ? node : "redis://" + node;
	}
}
