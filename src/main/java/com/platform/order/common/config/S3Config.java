package com.platform.order.common.config;

import java.net.URI;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.platform.order.common.config.constant.AwsProperty;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Profile({"prod"})
@RequiredArgsConstructor
@EnableConfigurationProperties(AwsProperty.class)
@Configuration
public class S3Config {

	private final AwsProperty awsProperty;

	@Bean
	public S3Client s3Client() {
		return S3Client.builder()
			.credentialsProvider(
				StaticCredentialsProvider.create(
					AwsBasicCredentials.create(awsProperty.accessKey(), awsProperty.secretKey())
				)
			)
			.region(Region.AP_NORTHEAST_2)
			.build();
	}
}
