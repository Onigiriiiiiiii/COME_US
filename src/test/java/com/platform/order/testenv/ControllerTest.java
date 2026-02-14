package com.platform.order.testenv;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.platform.order.authentication.service.AuthService;
import com.platform.order.common.security.oauth2.oauth2service.OAuth2Service;
import com.platform.order.common.security.oauth2.oauth2service.OAuth2UserService;
import com.platform.order.common.security.service.TokenService;

public class ControllerTest {
	@Autowired
	protected MockMvc mockMvc;

	protected final ObjectMapper objectMapper = JsonMapper.builder()
		.findAndAddModules()
		.build();

	@MockitoBean
	protected AuthService authService;

	@MockitoBean
	protected TokenService tokenService;

	@MockitoBean
	protected OAuth2Service oAuth2Service;

}
