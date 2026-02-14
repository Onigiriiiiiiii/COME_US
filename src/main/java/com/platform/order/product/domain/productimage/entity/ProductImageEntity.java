package com.platform.order.product.domain.productimage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.platform.order.common.superentity.FileBaseEntity;
import com.platform.order.product.domain.product.entity.ProductEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_image")
@Entity
public class ProductImageEntity extends FileBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long arrangement;

	@ManyToOne(fetch = FetchType.LAZY)
	private ProductEntity product;

	@Builder
	public ProductImageEntity(
		String originName,
		String fileName,
		String path,
		String extension,
		Long size,
		Long arrangement,
		ProductEntity product
	) {
		super(originName, fileName, path, extension, size);
		this.arrangement = arrangement;
		this.product = product;
	}
}
