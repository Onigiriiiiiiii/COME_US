package com.platform.order.common.storage;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.platform.order.common.exception.custom.BusinessException;
import com.platform.order.common.exception.model.ErrorCode;
import com.platform.order.common.storage.request.UploadFileRequestDto;
import com.platform.order.common.storage.response.UploadFileResponseDto;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@Service
public class AwsStorageService {

	private final S3Client s3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.s3.suffix-url}")
	private String suffixUrl;

	public String upload(MultipartFile multipartFile, FileSuffixPath path, String fileName, String extension) {
		String key = generateKey(path, fileName, extension);
		try (InputStream inputStream = multipartFile.getInputStream()) {
			s3Client.putObject(
				PutObjectRequest.builder()
					.bucket(bucket)
					.key(key)
					.contentType(multipartFile.getContentType())
					.contentLength(multipartFile.getSize())
					.build(),
				RequestBody.fromInputStream(inputStream, multipartFile.getSize())
			);
		} catch (IOException e) {
			rollback(List.of(key));
			throw new BusinessException(
				MessageFormat.format("upload fail : {0}  ", multipartFile.getOriginalFilename()),
				ErrorCode.FILE_IO
			);
		}

		return suffixUrl + key;
	}

	/**
	 * fileName,upload path 들을  전달한다
	 * @param fileRequestDto
	 * @param path
	 * @return
	 */
	public List<UploadFileResponseDto> upload(List<UploadFileRequestDto> fileRequestDto, FileSuffixPath path) {
		List<UploadFileResponseDto> fileResponses = new ArrayList<>();
		List<String> rollbacks = new ArrayList<>();

		for (var requestDto : fileRequestDto) {
			MultipartFile multipartFile = requestDto.multipartFile();

			String key = generateKey(path, requestDto.fileName(), requestDto.extension());
			try (InputStream inputStream = multipartFile.getInputStream()) {
				s3Client.putObject(
					PutObjectRequest.builder()
						.bucket(bucket)
						.key(key)
						.contentType(multipartFile.getContentType())
						.contentLength(multipartFile.getSize())
						.build(),
					RequestBody.fromInputStream(inputStream, multipartFile.getSize())
				);

				rollbacks.add(key);
			} catch (IOException e) {
				rollback(rollbacks);
				throw new BusinessException(
					MessageFormat.format("upload fail : {0}  ", multipartFile.getOriginalFilename()),
					ErrorCode.FILE_IO
				);
			} finally {
				fileResponses.add(new UploadFileResponseDto(requestDto.fileName(), key, requestDto.extension(),
					requestDto.multipartFile()));
			}
		}

		return fileResponses;
	}

	public String delete(FileSuffixPath path, String fullFileName) {
		String key = generateKey(path, fullFileName);
		s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());

		return suffixUrl + key;
	}

	public void deleteAll(FileSuffixPath path, List<String> fullFileNames) {
		fullFileNames.stream()
			.map(fullFileName -> generateKey(path, fullFileName))
			.forEach(key -> s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build()));
	}

	private void rollback(List<String> urlKeys) {
		urlKeys.forEach(key -> s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build()));
	}

	private String generateKey(FileSuffixPath path, String fileName) {
		return path.getPath() + fileName;
	}

	private String generateKey(FileSuffixPath path, String fileName, String extension) {
		return path.getPath() + fileName + "." + extension;
	}
}
