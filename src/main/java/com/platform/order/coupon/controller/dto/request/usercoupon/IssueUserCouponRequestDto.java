package com.platform.order.coupon.controller.dto.request.usercoupon;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

public record IssueUserCouponRequestDto(
	@Schema(description = "발급 받을 쿠폰 식별자", required = true)
	@NotNull
	@Positive Long couponId
) {
}
