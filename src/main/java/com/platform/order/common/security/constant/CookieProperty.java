package com.platform.order.common.security.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.server.Cookie;

@ConfigurationProperties(prefix = "cookie")
public record CookieProperty(
	Boolean secure,
	Cookie.SameSite sameSite,
	String domain
) {
}
