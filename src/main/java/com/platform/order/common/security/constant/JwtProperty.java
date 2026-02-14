package com.platform.order.common.security.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.platform.order.common.security.model.Token;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperty(
	Token accessToken,
	Token refreshToken,
	String issuer,
	String secretKey
) {
}
