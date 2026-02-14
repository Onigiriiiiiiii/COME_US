package com.platform.order.common.security.constant;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public record SecurityUrlProperty(UrlPatternConfig urlPatternConfig) {

	public record UrlPatternConfig(Map<String, String[]> ignoring, Map<String, String[]> permitAll) {
	}
}
