package com.platform.order.config;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @Document: aws s3 에뮬레이터
 */
@TestConfiguration
public class TestLocalStackS3Config {
	private static final DockerImageName LOCAL_STACK_IMAGE = DockerImageName.parse("localstack/localstack");

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Bean(initMethod = "start", destroyMethod = "stop")
	public LocalStackContainer localStackContainer() {
		return new LocalStackContainer(LOCAL_STACK_IMAGE)
			.withServices(LocalStackContainer.Service.S3);
	}

	@Bean
	public software.amazon.awssdk.services.s3.S3Client s3Client(LocalStackContainer localStackContainer) {
		software.amazon.awssdk.services.s3.S3Client s3Client =
			software.amazon.awssdk.services.s3.S3Client.builder()
			.endpointOverride(URI.create(localStackContainer.getEndpointOverride(S3).toString()))
			.credentialsProvider(
				software.amazon.awssdk.auth.credentials.StaticCredentialsProvider.create(
					software.amazon.awssdk.auth.credentials.AwsBasicCredentials.create(
						localStackContainer.getAccessKey(),
						localStackContainer.getSecretKey()
					)
				)
			)
			.region(software.amazon.awssdk.regions.Region.of(localStackContainer.getRegion()))
			.serviceConfiguration(
				software.amazon.awssdk.services.s3.S3Configuration.builder()
					.pathStyleAccessEnabled(true)
					.build()
			)
			.build();

		s3Client.createBucket(
			software.amazon.awssdk.services.s3.model.CreateBucketRequest.builder()
				.bucket(bucket)
				.build()
		);

		return s3Client;
	}
}
