package com.platform.order.product.domain.category.entity;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.SQLRestriction;

import com.platform.order.common.superentity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted=false")
@Table(name = "category")
@Entity
public class CategoryEntity extends BaseEntity {
	private String name;
	private String code;

	@ManyToOne(fetch = FetchType.LAZY)
	private CategoryEntity parent;

	@OneToMany(mappedBy = "parent")
	private List<CategoryEntity> childs;

	public Optional<CategoryEntity> getParent() {
		return Optional.ofNullable(this.parent);
	}
}
