package com.platform.order.product.domain.productthumbnail.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.platform.order.common.superentity.FileBaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_thunmnail_image")
@Entity
public class ProductThumbnailEntity extends FileBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Builder
	public ProductThumbnailEntity(
		String originName,
		String fileName,
		String path,
		String extension,
		Long size,
		Long id
	) {
		super(originName, fileName, path, extension, size);
		this.id = id;
	}

}
