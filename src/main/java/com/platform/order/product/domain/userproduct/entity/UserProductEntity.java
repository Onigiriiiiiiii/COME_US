package com.platform.order.product.domain.userproduct.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.platform.order.product.domain.product.entity.ProductEntity;
import com.platform.order.user.domain.entity.UserEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user_product")
@Entity
public class UserProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private UserEntity wisher;

	@ManyToOne(fetch = FetchType.LAZY)
	private ProductEntity product;

	@Builder.Default
	private LocalDate createdAt = LocalDate.now();

	public boolean isWisher(UserEntity wisher) {
		return this.wisher.equals(wisher);
	}

}
