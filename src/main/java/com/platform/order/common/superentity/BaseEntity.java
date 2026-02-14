package com.platform.order.common.superentity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Column(updatable = false, nullable = false)
	@CreatedDate
	protected LocalDateTime createdAt;

	@Column(nullable = false)
	@LastModifiedDate
	protected LocalDateTime updatedAt;

	@Column(name = "deleted", nullable = false)
	protected Boolean isDeleted = false;
}
