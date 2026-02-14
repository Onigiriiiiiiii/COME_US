package com.platform.order.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import org.hibernate.annotations.SQLRestriction;

import com.platform.order.common.superentity.BaseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("deleted=false")
@Table(name = "users")
@Entity
public class UserEntity extends BaseEntity {

	private String username;

	private String password;

	private String nickName;

	private String email;

	private String provider;

	private String providerId;

	@Enumerated(EnumType.STRING)
	private Role role;
}

