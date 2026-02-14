package com.platform.order.common.config.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "cloud.aws.s3")
public record LocalStackProperty(String endpoint, String bucket) {
	@ConstructorBinding
	public LocalStackProperty {
	}
}
