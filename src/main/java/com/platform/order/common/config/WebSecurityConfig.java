package com.platform.order.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import com.platform.order.common.security.JwtAuthenticationFilter;
import com.platform.order.common.security.JwtProviderManager;
import com.platform.order.common.security.constant.CookieProperty;
import com.platform.order.common.security.constant.JwtProperty;
import com.platform.order.common.security.constant.SecurityUrlProperty;
import com.platform.order.common.security.oauth2.Oauth2AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({SecurityUrlProperty.class, JwtProperty.class, CookieProperty.class})
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig {

	private final JwtProviderManager jwtProviderManager;
	private final CookieProperty cookieProperty;
	private final SecurityUrlProperty securityUrlProperty;
	private final JwtProperty jwtProperty;
	private final Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> {
			WebSecurity.IgnoredRequestConfigurer ignoring = web.ignoring();
			ignore(ignoring, HttpMethod.GET);
			ignore(ignoring, HttpMethod.POST);
			ignore(ignoring, HttpMethod.PATCH);
			ignore(ignoring, HttpMethod.PUT);
			ignore(ignoring, HttpMethod.DELETE);
		};
	}

	@Bean
	AuthenticationEntryPoint authenticationEntryPoint() {
		return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.authorizeHttpRequests(registry -> {
				registry.requestMatchers(CorsUtils::isPreFlightRequest).permitAll();
				permitAll(registry, HttpMethod.GET);
				permitAll(registry, HttpMethod.POST);
				permitAll(registry, HttpMethod.PATCH);
				permitAll(registry, HttpMethod.PUT);
				permitAll(registry, HttpMethod.DELETE);
				permitAll(registry, HttpMethod.OPTIONS);
				registry.anyRequest().authenticated();
			})
			.formLogin(formLogin -> formLogin.disable())
			.csrf(csrf -> csrf.disable())
			.headers(headers -> headers.disable())
			.httpBasic(httpBasic -> httpBasic.disable())
			.rememberMe(rememberMe -> rememberMe.disable())
			.logout(logout -> logout.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(handling -> handling.authenticationEntryPoint(authenticationEntryPoint()))
			.addFilterBefore(
				new JwtAuthenticationFilter(jwtProviderManager, jwtProperty, cookieProperty),
				UsernamePasswordAuthenticationFilter.class
			)
			.cors(Customizer.withDefaults())
			.oauth2Login(oauth2 -> oauth2.successHandler(oauth2AuthenticationSuccessHandler));

		return httpSecurity.build();
	}

	private void ignore(WebSecurity.IgnoredRequestConfigurer ignoring, HttpMethod httpMethod) {
		String[] urls = getIgnoringUrl(httpMethod);
		if (urls.length > 0) {
			ignoring.requestMatchers(httpMethod, urls);
		}
	}

	private void permitAll(
		AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry,
		HttpMethod httpMethod
	) {
		String[] urls = getPermitAllUrl(httpMethod);
		if (urls.length > 0) {
			registry.requestMatchers(httpMethod, urls).permitAll();
		}
	}

	private String[] getIgnoringUrl(HttpMethod httpMethod) {
		return getUrls(this.securityUrlProperty.urlPatternConfig().ignoring(), httpMethod);
	}

	private String[] getPermitAllUrl(HttpMethod httpMethod) {
		return getUrls(this.securityUrlProperty.urlPatternConfig().permitAll(), httpMethod);
	}

	private String[] getUrls(java.util.Map<String, String[]> urls, HttpMethod httpMethod) {
		String[] values = urls.get(httpMethod.name());
		return values == null ? new String[0] : values;
	}
}
