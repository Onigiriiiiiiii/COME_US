package com.platform.order.coupon.domain.coupon.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.SQLRestriction;

import com.platform.order.common.superentity.BaseEntity;
import com.platform.order.coupon.domain.usercoupon.entity.Calculable;
import com.platform.order.user.domain.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted=false")
@Table(name = "coupon")
@Entity
public class CouponEntity extends BaseEntity implements Calculable {
	@Enumerated(EnumType.STRING)
	private CouponType type;

	private Long amount;

	private Long quantity;

	private LocalDate expiredAt;

	@ManyToOne(fetch = FetchType.LAZY)
	private UserEntity user;

	@Override
	public long discount(Long price) {
		return type.apply(price, this.amount);
	}
}
