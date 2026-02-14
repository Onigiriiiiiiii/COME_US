package com.platform.order.common.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.platform.order.common.config.constant.AwsProperty;
import com.platform.order.common.config.constant.LocalStackProperty;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Profile("local")
@RequiredArgsConstructor
@EnableConfigurationProperties({LocalStackProperty.class, AwsProperty.class})
@Configuration
public class LocalStackConfig {

	private final AwsProperty awsProperty;

	private final LocalStackProperty localStackProperty;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Bean
	public S3Client s3Client() {
		S3Client s3Client = S3Client.builder()
			.endpointOverride(URI.create(localStackProperty.endpoint()))
			.credentialsProvider(
				StaticCredentialsProvider.create(
					AwsBasicCredentials.create(awsProperty.accessKey(), awsProperty.secretKey())
				)
			)
			.region(Region.of(region))
			.serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
			.build();

		try {
			s3Client.headBucket(HeadBucketRequest.builder().bucket(localStackProperty.bucket()).build());
		} catch (S3Exception exception) {
			s3Client.createBucket(CreateBucketRequest.builder().bucket(localStackProperty.bucket()).build());
		}

		return s3Client;
	}
}
