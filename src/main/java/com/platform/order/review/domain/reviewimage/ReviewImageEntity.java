package com.platform.order.review.domain.reviewimage;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.platform.order.common.superentity.FileBaseEntity;
import com.platform.order.review.domain.review.entity.ReviewEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "review_image")
@Entity
public class ReviewImageEntity extends FileBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private ReviewEntity review;

	@Builder
	public ReviewImageEntity(
		String originName,
		String fileName,
		String path,
		String extension,
		Long size
	) {
		super(originName, fileName, path, extension, size);
	}

	public void addReview(ReviewEntity review) {
		this.review = review;
	}
}
